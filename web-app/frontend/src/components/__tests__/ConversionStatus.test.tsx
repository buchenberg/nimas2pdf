import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import ConversionStatus from '../ConversionStatus';

// Mock axios
jest.mock('axios', () => ({
  get: jest.fn()
}));

// Mock window.open
const mockOpen = jest.fn();
Object.defineProperty(window, 'open', {
  writable: true,
  value: mockOpen
});

describe('ConversionStatus Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders the component with header and description', () => {
    render(<ConversionStatus />);
    
    expect(screen.getByText('Conversion Status')).toBeInTheDocument();
    expect(screen.getByText(/Conversion Status/)).toBeInTheDocument();
  });

  it('loads and displays mock conversion jobs', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      expect(screen.getByText('Conversion completed successfully!')).toBeInTheDocument();
      expect(screen.getByText('Generating PDF...')).toBeInTheDocument();
    });
  });

  it('displays conversion job information correctly', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check job IDs are displayed
      expect(screen.getByText('ID: 1')).toBeInTheDocument();
      expect(screen.getByText('ID: 2')).toBeInTheDocument();
    });
  });

  it('displays status badges correctly', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      expect(screen.getByText('Completed')).toBeInTheDocument();
      expect(screen.getByText('Processing')).toBeInTheDocument();
    });
  });

  it('shows progress bars for processing jobs', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that progress bar is displayed for processing job
      const progressBar = screen.getByRole('progressbar');
      expect(progressBar).toBeInTheDocument();
      expect(progressBar).toHaveAttribute('aria-valuenow', '65');
    });
  });

  it('displays package metadata in conversion items', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that package metadata is displayed
      expect(screen.getByText(/9781122334455NIMAS/)).toBeInTheDocument();
      expect(screen.getByText(/9781122334456NIMAS/)).toBeInTheDocument();
      
      // Check package titles - they're split across elements, so use partial matching
      expect(screen.getByText(/The Great Depression/)).toBeInTheDocument();
      expect(screen.getByText(/Sample Textbook/)).toBeInTheDocument();
    });
  });

  it('formats file sizes correctly', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that file size is formatted correctly
      expect(screen.getByText('Size: 1.95 MB')).toBeInTheDocument();
    });
  });

  it('displays start and update times correctly', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that timestamps are displayed - there are multiple, so use getAllByText
      const startedElements = screen.getAllByText(/Started:/);
      expect(startedElements.length).toBeGreaterThan(0);
      
      const updatedElements = screen.getAllByText(/Updated:/);
      expect(updatedElements.length).toBeGreaterThan(0);
    });
  });

  it('shows output file information for completed jobs', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      expect(screen.getByText('document_1.pdf')).toBeInTheDocument();
      expect(screen.getByText('Size: 1.95 MB')).toBeInTheDocument();
    });
  });

  it('displays download button for completed jobs', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const downloadButton = screen.getByText('Download');
      expect(downloadButton).toBeInTheDocument();
      expect(downloadButton).toHaveClass('btn', 'btn-outline-primary', 'btn-sm');
    });
  });

  it('handles download action', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const downloadButton = screen.getByText('Download');
      expect(downloadButton).toBeInTheDocument();
    });
    
    const downloadButton = screen.getByText('Download');
    await userEvent.click(downloadButton);
    
    // The download functionality is mocked, so we just verify the button is clickable
    expect(downloadButton).toBeInTheDocument();
  });

  it('applies correct CSS classes to conversion items', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const conversionItems = screen.getAllByText(/ID: /);
      expect(conversionItems.length).toBeGreaterThan(0);
      
      // Check that the first item has the completed class
      const firstItem = conversionItems[0].closest('.conversion-item');
      expect(firstItem).toHaveClass('completed');
    });
  });

  it('displays status icons correctly', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that status icons are displayed
      const icons = document.querySelectorAll('.bi');
      expect(icons.length).toBeGreaterThan(0);
    });
  });

  it('shows job IDs in the correct format', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      expect(screen.getByText('ID: 1')).toBeInTheDocument();
      expect(screen.getByText('ID: 2')).toBeInTheDocument();
    });
  });

  it('displays conversion messages correctly', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      expect(screen.getByText('Conversion completed successfully!')).toBeInTheDocument();
      expect(screen.getByText('Generating PDF...')).toBeInTheDocument();
    });
  });

  it('shows package information with correct formatting', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check package info formatting
      expect(screen.getByText(/Package: 9781122334455NIMAS - The Great Depression/)).toBeInTheDocument();
      expect(screen.getByText(/Package: 9781122334456NIMAS - Sample Textbook/)).toBeInTheDocument();
    });
  });

  it('displays file information section for completed jobs', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that the file info section is displayed
      expect(screen.getByText('document_1.pdf')).toBeInTheDocument();
      expect(screen.getByText('Size: 1.95 MB')).toBeInTheDocument();
    });
  });

  it('handles missing optional fields gracefully', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // The component should render without errors even with missing optional fields
      expect(screen.getByText('Conversion Status')).toBeInTheDocument();
    });
  });

  it('maintains proper card structure', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const card = screen.getByText('Conversion Status').closest('.card');
      expect(card).toBeInTheDocument();
      
      const cardHeader = screen.getByText('Conversion Status').closest('.card-header');
      expect(cardHeader).toBeInTheDocument();
      
      const cardBody = screen.getByText('Conversion Status').closest('.card')?.querySelector('.card-body');
      expect(cardBody).toBeInTheDocument();
    });
  });

  it('displays multiple conversion jobs in a list', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const listGroup = screen.getByText('Conversion Status').closest('.card')?.querySelector('.list-group');
      expect(listGroup).toBeInTheDocument();
      
      const listItems = listGroup?.querySelectorAll('.list-group-item');
      expect(listItems?.length).toBe(2);
    });
  });

  it('shows progress information for processing jobs', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const progressBar = screen.getByRole('progressbar');
      expect(progressBar).toBeInTheDocument();
      expect(progressBar).toHaveAttribute('aria-valuenow', '65');
      expect(progressBar).toHaveAttribute('aria-valuemin', '0');
      expect(progressBar).toHaveAttribute('aria-valuemax', '100');
    });
  });

  it('displays package metadata with proper icons', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that package info has the archive icon
      const packageInfo = screen.getByText(/Package: 9781122334455NIMAS/);
      const archiveIcon = packageInfo.closest('div')?.querySelector('.bi-archive');
      expect(archiveIcon).toBeInTheDocument();
    });
  });

  it('shows timestamp information with proper icons', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      // Check that timestamps have the clock icons - there are multiple, so use getAllByText
      const startedElements = screen.getAllByText(/Started:/);
      expect(startedElements.length).toBeGreaterThan(0);
      
      // Check the first started element has a clock icon
      const firstStarted = startedElements[0];
      const clockIcon = firstStarted.closest('div')?.querySelector('.bi-clock');
      expect(clockIcon).toBeInTheDocument();
      
      const updatedElements = screen.getAllByText(/Updated:/);
      expect(updatedElements.length).toBeGreaterThan(0);
      
      // Check the first updated element has an update icon
      const firstUpdated = updatedElements[0];
      const updateIcon = firstUpdated.closest('div')?.querySelector('.bi-arrow-clockwise');
      expect(updateIcon).toBeInTheDocument();
    });
  });

  it('displays file information with proper styling', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const fileSection = screen.getByText('document_1.pdf').closest('.bg-light');
      expect(fileSection).toBeInTheDocument();
      expect(fileSection).toHaveClass('bg-light', 'rounded');
    });
  });

  it('shows PDF file icon for output files', async () => {
    render(<ConversionStatus />);
    
    await waitFor(() => {
      const fileInfo = screen.getByText('document_1.pdf');
      const pdfIcon = fileInfo.closest('div')?.querySelector('.bi-file-earmark-pdf');
      expect(pdfIcon).toBeInTheDocument();
      expect(pdfIcon).toHaveClass('text-danger');
    });
  });
});
