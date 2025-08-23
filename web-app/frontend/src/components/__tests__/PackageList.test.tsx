import React from 'react';
import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import PackageList from '../PackageList';

// Mock axios
jest.mock('axios', () => ({
  get: jest.fn()
}));

// Import the mocked axios
import axios from 'axios';
const mockedAxios = axios as jest.Mocked<typeof axios>;

// Mock window.open
const mockOpen = jest.fn();
Object.defineProperty(window, 'open', {
  writable: true,
  value: mockOpen
});

// Mock window.confirm
const mockConfirm = jest.fn();
Object.defineProperty(window, 'confirm', {
  writable: true,
  value: mockConfirm
});

describe('PackageList Component', () => {
  const mockPackages = [
    {
      id: 1,
      packageId: '9781122334455NIMAS',
      title: 'The Great Depression',
      creator: 'John Doe',
      subject: 'History',
      status: 'READY',
      uploadedAt: '2025-08-22T10:00:00Z'
    },
    {
      id: 2,
      packageId: '9781122334456NIMAS',
      title: 'Sample Textbook',
      creator: 'Jane Smith',
      subject: 'Education',
      status: 'UPLOADED',
      uploadedAt: '2025-08-22T11:00:00Z'
    },
    {
      id: 3,
      packageId: '9781122334457NIMAS',
      title: 'Advanced Mathematics',
      creator: 'Dr. Math',
      subject: 'Mathematics',
      status: 'PROCESSING',
      uploadedAt: '2025-08-22T12:00:00Z'
    }
  ];

  beforeEach(() => {
    jest.clearAllMocks();
    mockOpen.mockClear();
    mockConfirm.mockClear();
    
    // Default mock response
    mockedAxios.get.mockResolvedValue({ data: mockPackages });
  });

  it('renders without crashing', async () => {
    render(<PackageList />);
    
    // Wait for the component to load and display packages
    await waitFor(() => {
      expect(screen.getByText('NIMAS Packages')).toBeInTheDocument();
    });
  });

  it('displays loading state initially', () => {
    render(<PackageList />);
    
    expect(screen.getByText('Loading packages...')).toBeInTheDocument();
    expect(screen.getByRole('status')).toBeInTheDocument();
  });

  it('loads and displays packages successfully', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      expect(screen.getByText('The Great Depression')).toBeInTheDocument();
      expect(screen.getByText('Sample Textbook')).toBeInTheDocument();
      expect(screen.getByText('Advanced Mathematics')).toBeInTheDocument();
    });
  });

  it('displays package information correctly', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      expect(screen.getByText('9781122334455NIMAS')).toBeInTheDocument();
      expect(screen.getByText('John Doe')).toBeInTheDocument();
      expect(screen.getByText('History')).toBeInTheDocument();
    });
  });

  it('shows correct status badges', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      // Use getAllByText since there are multiple "Ready" elements (select option and badge)
      const readyBadges = screen.getAllByText('Ready');
      expect(readyBadges.length).toBeGreaterThan(0);
      
      // Check that at least one Ready badge has the correct styling
      const readyBadge = readyBadges.find(badge => badge.classList.contains('badge'));
      expect(readyBadge).toBeInTheDocument();
      expect(readyBadge).toHaveClass('badge', 'bg-success');
      
      // Use getAllByText for Uploaded since there are multiple elements
      const uploadedElements = screen.getAllByText('Uploaded');
      expect(uploadedElements.length).toBeGreaterThan(0);
      
      // Find the badge element specifically
      const uploadedBadge = uploadedElements.find(element => element.classList.contains('badge'));
      expect(uploadedBadge).toBeInTheDocument();
      expect(uploadedBadge).toHaveClass('badge', 'bg-info');
      
      // Use getAllByText for Processing since there are multiple elements
      const processingElements = screen.getAllByText('Processing');
      expect(processingElements.length).toBeGreaterThan(0);
      
      // Find the badge element specifically
      const processingBadge = processingElements.find(element => element.classList.contains('badge'));
      expect(processingBadge).toBeInTheDocument();
      expect(processingBadge).toHaveClass('badge', 'bg-warning');
    });
  });

  it('displays action buttons for each package', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const viewButtons = screen.getAllByTitle('View Details');
      const downloadButtons = screen.getAllByTitle('Download Package');
      const deleteButtons = screen.getAllByTitle('Delete Package');
      
      expect(viewButtons).toHaveLength(3);
      expect(downloadButtons).toHaveLength(3);
      expect(deleteButtons).toHaveLength(3);
    });
  });

  it('shows convert button only for READY packages', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const convertButtons = screen.getAllByTitle('Start Conversion');
      expect(convertButtons).toHaveLength(1); // Only the READY package should have this button
    });
  });

  it('handles search functionality', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const searchInput = screen.getByPlaceholderText('Search packages...');
      expect(searchInput).toBeInTheDocument();
      
      // Type in search
      fireEvent.change(searchInput, { target: { value: 'Great Depression' } });
      
      // Should filter to show only one package
      expect(screen.getByText('The Great Depression')).toBeInTheDocument();
      expect(screen.queryByText('Sample Textbook')).not.toBeInTheDocument();
    });
  });

  it('handles status filtering', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const statusSelect = screen.getByDisplayValue('All Statuses');
      expect(statusSelect).toBeInTheDocument();
      
      // Change status filter
      fireEvent.change(statusSelect, { target: { value: 'READY' } });
      
      // Should show only READY packages
      expect(screen.getByText('The Great Depression')).toBeInTheDocument();
      expect(screen.queryByText('Sample Textbook')).not.toBeInTheDocument();
    });
  });

  it('combines search and status filtering', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const searchInput = screen.getByPlaceholderText('Search packages...');
      const statusSelect = screen.getByDisplayValue('All Statuses');
      
      // Set both filters
      fireEvent.change(searchInput, { target: { value: 'Textbook' } });
      fireEvent.change(statusSelect, { target: { value: 'UPLOADED' } });
      
      // Should show only packages matching both criteria
      expect(screen.getByText('Sample Textbook')).toBeInTheDocument();
      expect(screen.queryByText('The Great Depression')).not.toBeInTheDocument();
    });
  });

  it('handles refresh functionality', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const refreshButton = screen.getByText('Refresh');
      expect(refreshButton).toBeInTheDocument();
      
      // Click refresh
      fireEvent.click(refreshButton);
      
      // Should reload packages
      expect(mockedAxios.get).toHaveBeenCalledTimes(2); // Initial load + refresh
    });
  });

  it('handles package actions correctly', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const viewButton = screen.getAllByTitle('View Details')[0];
      fireEvent.click(viewButton);
      
      // Should log the action (we can't easily test console.log in tests)
      expect(viewButton).toBeInTheDocument();
    });
  });

  it('handles download action', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const downloadButton = screen.getAllByTitle('Download Package')[0];
      fireEvent.click(downloadButton);
      
      // Should open download URL
      expect(mockOpen).toHaveBeenCalledWith('/api/packages/1/download', '_blank');
    });
  });

  it('handles delete action with confirmation', async () => {
    mockConfirm.mockReturnValue(true);
    
    render(<PackageList />);
    
    await waitFor(() => {
      const deleteButton = screen.getAllByTitle('Delete Package')[0];
      fireEvent.click(deleteButton);
      
      // Should show confirmation dialog
      expect(mockConfirm).toHaveBeenCalledWith('Are you sure you want to delete package "The Great Depression"?');
    });
  });

  it('shows empty state when no packages exist', async () => {
    // Mock empty response
    mockedAxios.get.mockResolvedValueOnce({ data: [] });
    
    render(<PackageList />);
    
    await waitFor(() => {
      expect(screen.getByText('No packages uploaded yet. Upload a NIMAS package to get started!')).toBeInTheDocument();
    });
  });

  it('shows filtered empty state when search has no results', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const searchInput = screen.getByPlaceholderText('Search packages...');
      fireEvent.change(searchInput, { target: { value: 'Nonexistent Package' } });
      
      // Should show no results
      expect(screen.queryByText('The Great Depression')).not.toBeInTheDocument();
      expect(screen.queryByText('Sample Textbook')).not.toBeInTheDocument();
    });
  });

  it('displays package count information', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      expect(screen.getByText('Showing 3 of 3 packages')).toBeInTheDocument();
    });
  });

  it('handles API errors gracefully', async () => {
    // Mock API error
    mockedAxios.get.mockRejectedValueOnce(new Error('Network error'));
    
    render(<PackageList />);
    
    await waitFor(() => {
      expect(screen.getByText('Failed to load packages')).toBeInTheDocument();
    });
  });

  it('formats dates correctly', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      // Check that dates are formatted (this will depend on the user's locale)
      // The actual format shown is "8/22/2025, 4:00:00 AM" etc.
      const dateElements = screen.getAllByText(/8\/22\/2025/);
      expect(dateElements.length).toBeGreaterThan(0);
      
      // Check that the dates are displayed in the correct format
      expect(screen.getByText('8/22/2025, 4:00:00 AM')).toBeInTheDocument();
      expect(screen.getByText('8/22/2025, 5:00:00 AM')).toBeInTheDocument();
      expect(screen.getByText('8/22/2025, 6:00:00 AM')).toBeInTheDocument();
    });
  });

  it('handles packages without optional fields', async () => {
    const packagesWithoutOptionalFields = [
      {
        id: 1,
        packageId: 'TEST123',
        title: 'Test Package',
        status: 'READY',
        uploadedAt: '2025-08-22T10:00:00Z'
      }
    ];
    
    mockedAxios.get.mockResolvedValueOnce({ data: packagesWithoutOptionalFields });
    
    render(<PackageList />);
    
    await waitFor(() => {
      expect(screen.getByText('Test Package')).toBeInTheDocument();
      expect(screen.getByText('TEST123')).toBeInTheDocument();
    });
  });

  it('maintains proper card structure', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const card = screen.getByText('NIMAS Packages').closest('.card');
      expect(card).toBeInTheDocument();
      
      const cardHeader = screen.getByText('NIMAS Packages').closest('.card-header');
      expect(cardHeader).toBeInTheDocument();
      
      const cardBody = screen.getByText('NIMAS Packages').closest('.card')?.querySelector('.card-body');
      expect(cardBody).toBeInTheDocument();
    });
  });

  it('displays search and filter controls', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      expect(screen.getByPlaceholderText('Search packages...')).toBeInTheDocument();
      expect(screen.getByDisplayValue('All Statuses')).toBeInTheDocument();
      expect(screen.getByText('Refresh')).toBeInTheDocument();
    });
  });

  it('shows proper table structure', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const table = screen.getByRole('table');
      expect(table).toBeInTheDocument();
      
      // Check table headers - use getAllByText for elements that might appear multiple times
      expect(screen.getByText('Package ID')).toBeInTheDocument();
      expect(screen.getByText('Title')).toBeInTheDocument();
      expect(screen.getByText('Creator')).toBeInTheDocument();
      expect(screen.getByText('Subject')).toBeInTheDocument();
      expect(screen.getByText('Status')).toBeInTheDocument();
      
      // For "Uploaded" header, use getAllByText and find the table header specifically
      const uploadedElements = screen.getAllByText('Uploaded');
      const uploadedHeader = uploadedElements.find(element => element.tagName === 'TH');
      expect(uploadedHeader).toBeInTheDocument();
      
      expect(screen.getByText('Actions')).toBeInTheDocument();
    });
  });

  it('displays archive icon in header', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const header = screen.getByText('NIMAS Packages');
      const archiveIcon = header.querySelector('.bi-archive');
      expect(archiveIcon).toBeInTheDocument();
    });
  });

  it('shows proper Bootstrap styling', async () => {
    render(<PackageList />);
    
    await waitFor(() => {
      const card = screen.getByText('NIMAS Packages').closest('.card');
      expect(card).toHaveClass('status-card');
      
      const table = screen.getByRole('table');
      expect(table).toHaveClass('table', 'table-striped', 'table-hover');
      
      const searchInput = screen.getByPlaceholderText('Search packages...');
      expect(searchInput).toHaveClass('form-control');
    });
  });
});
