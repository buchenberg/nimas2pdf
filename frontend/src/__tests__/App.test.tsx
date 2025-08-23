import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import App from '../App';

// Mock the child components to isolate App component testing
jest.mock('../components/PackageUpload', () => {
  return function MockPackageUpload({ onPackageUploaded }: any) {
    return (
      <div data-testid="package-upload">
        <h3>Package Upload Component</h3>
        <button onClick={() => onPackageUploaded && onPackageUploaded('TEST123')}>
          Mock Upload
        </button>
      </div>
    );
  };
});

jest.mock('../components/PackageList', () => {
  return function MockPackageList({ refreshTrigger }: any) {
    return (
      <div data-testid="package-list">
        <h3>Package List Component</h3>
        <span data-testid="refresh-trigger">{refreshTrigger}</span>
      </div>
    );
  };
});

jest.mock('../components/ConversionStatus', () => {
  return function MockConversionStatus() {
    return (
      <div data-testid="conversion-status">
        <h3>Conversion Status Component</h3>
      </div>
    );
  };
});



describe('App Component', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders without crashing', () => {
    render(<App />);
    expect(screen.getByText('NIMAS2PDF Web Application')).toBeInTheDocument();
  });

  it('displays the main heading and description', () => {
    render(<App />);
    
    expect(screen.getByText('NIMAS2PDF Web Application')).toBeInTheDocument();
    expect(screen.getByText(/Upload, manage, and convert NIMAS packages to accessible PDF format/)).toBeInTheDocument();
  });

  it('renders the tabbed interface', () => {
    render(<App />);
    
    // Check that all tabs are present
    expect(screen.getByText('Upload')).toBeInTheDocument();
    expect(screen.getByText('NIMAS Packages')).toBeInTheDocument();
    expect(screen.getByText('Conversion Jobs')).toBeInTheDocument();
  });

  it('shows the Upload tab by default', () => {
    render(<App />);
    
    // Default tab should be active
    const uploadTab = screen.getByText('Upload').closest('.nav-link');
    expect(uploadTab).toHaveClass('active');
    
    // Package upload component should be visible
    expect(screen.getByTestId('package-upload')).toBeInTheDocument();
  });

  it('switches to Upload tab when clicked', async () => {
    const user = userEvent.setup();
    render(<App />);
    
    // First switch to another tab
    const packagesTab = screen.getByText('NIMAS Packages');
    await user.click(packagesTab);
    
    // Then switch back to Upload tab
    const uploadTab = screen.getByText('Upload');
    await user.click(uploadTab);
    
    // Upload tab should now be active
    expect(uploadTab.closest('.nav-link')).toHaveClass('active');
    
    // Package upload component should be visible
    expect(screen.getByTestId('package-upload')).toBeInTheDocument();
  });

  it('switches to Conversion Jobs tab when clicked', async () => {
    const user = userEvent.setup();
    render(<App />);
    
    const conversionJobsTab = screen.getByText('Conversion Jobs');
    await user.click(conversionJobsTab);
    
    // Conversion Jobs tab should now be active
    expect(conversionJobsTab.closest('.nav-link')).toHaveClass('active');
    
    // Conversion status component should be visible
    expect(screen.getByTestId('conversion-status')).toBeInTheDocument();
  });



  it('shows appropriate info panels for each tab', () => {
    render(<App />);
    
    // Check info panel for Upload tab (default active)
    expect(screen.getByText(/Upload NIMAS Package/)).toBeInTheDocument();
    expect(screen.getByText(/Upload a NIMAS ZIP package to begin the conversion process/)).toBeInTheDocument();
    
    // Check info panel for NIMAS Packages tab
    expect(screen.getByText(/About NIMAS Packages/)).toBeInTheDocument();
    expect(screen.getByText(/View and manage your uploaded NIMAS packages/)).toBeInTheDocument();
  });

  it('displays the success message about database features', () => {
    render(<App />);
    
    expect(screen.getByText(/New!/)).toBeInTheDocument();
    expect(screen.getByText(/Database-backed package management with persistent storage and enterprise features/)).toBeInTheDocument();
  });

  it('renders PackageUpload component with callback', () => {
    render(<App />);
    
    const packageUpload = screen.getByTestId('package-upload');
    expect(packageUpload).toBeInTheDocument();
    expect(screen.getByText('Package Upload Component')).toBeInTheDocument();
  });

  it('renders PackageList component with refresh trigger', () => {
    render(<App />);
    
    const packageList = screen.getByTestId('package-list');
    expect(packageList).toBeInTheDocument();
    expect(screen.getByText('Package List Component')).toBeInTheDocument();
    
    // Check that refresh trigger is passed
    const refreshTrigger = screen.getByTestId('refresh-trigger');
    expect(refreshTrigger).toHaveTextContent('0');
  });

  it('handles package upload callback and updates refresh trigger', async () => {
    const user = userEvent.setup();
    render(<App />);
    
    // Initially refresh trigger should be 0
    expect(screen.getByTestId('refresh-trigger')).toHaveTextContent('0');
    
    // Click the mock upload button
    const uploadButton = screen.getByText('Mock Upload');
    await user.click(uploadButton);
    
    // Refresh trigger should be incremented
    expect(screen.getByTestId('refresh-trigger')).toHaveTextContent('1');
  });

  it('maintains tab state when switching between tabs', async () => {
    const user = userEvent.setup();
    render(<App />);
    
    // Start on NIMAS Packages tab
    expect(screen.getByTestId('package-upload')).toBeInTheDocument();
    
    // Switch to Conversion Jobs tab
    const conversionJobsTab = screen.getByText('Conversion Jobs');
    await user.click(conversionJobsTab);
    expect(screen.getByTestId('conversion-status')).toBeInTheDocument();
    
    // Switch back to NIMAS Packages tab
    const packagesTab = screen.getByText('NIMAS Packages');
    await user.click(packagesTab);
    expect(screen.getByTestId('package-upload')).toBeInTheDocument();
    expect(screen.getByTestId('package-list')).toBeInTheDocument();
  });

  it('displays tab icons correctly', () => {
    render(<App />);
    
    // Check that icons are present in tab labels
    const uploadTab = screen.getByText('Upload');
    const packagesTab = screen.getByText('NIMAS Packages');
    const conversionJobsTab = screen.getByText('Conversion Jobs');
    
    expect(uploadTab).toBeInTheDocument();
    expect(packagesTab).toBeInTheDocument();
    expect(conversionJobsTab).toBeInTheDocument();
  });

  it('shows appropriate content for each tab', () => {
    render(<App />);
    
    // Upload tab content (default active)
    expect(screen.getByTestId('package-upload')).toBeInTheDocument();
    
    // Switch to NIMAS Packages tab
    const packagesTab = screen.getByText('NIMAS Packages');
    fireEvent.click(packagesTab);
    expect(screen.getByTestId('package-list')).toBeInTheDocument();
    
    // Switch to Conversion Jobs tab
    const conversionJobsTab = screen.getByText('Conversion Jobs');
    fireEvent.click(conversionJobsTab);
    expect(screen.getByTestId('conversion-status')).toBeInTheDocument();
  });

  it('handles multiple package uploads correctly', async () => {
    const user = userEvent.setup();
    render(<App />);
    
    // Initially refresh trigger should be 0
    expect(screen.getByTestId('refresh-trigger')).toHaveTextContent('0');
    
    // Upload first package
    const uploadButton = screen.getByText('Mock Upload');
    await user.click(uploadButton);
    expect(screen.getByTestId('refresh-trigger')).toHaveTextContent('1');
    
    // Upload second package
    await user.click(uploadButton);
    expect(screen.getByTestId('refresh-trigger')).toHaveTextContent('2');
  });

  it('maintains component state across tab switches', async () => {
    const user = userEvent.setup();
    render(<App />);
    
    // Upload a package
    const uploadButton = screen.getByText('Mock Upload');
    await user.click(uploadButton);
    expect(screen.getByTestId('refresh-trigger')).toHaveTextContent('1');
    
    // Switch to another tab
    const conversionJobsTab = screen.getByText('Conversion Jobs');
    await user.click(conversionJobsTab);
    
    // Switch back to packages tab
    const packagesTab = screen.getByText('NIMAS Packages');
    await user.click(packagesTab);
    
    // State should be maintained
    expect(screen.getByTestId('refresh-trigger')).toHaveTextContent('1');
  });

  it('renders with proper Bootstrap classes and styling', () => {
    render(<App />);
    
    // Check that Bootstrap classes are applied
    const container = screen.getByText('NIMAS2PDF Web Application').closest('.container-fluid');
    expect(container).toBeInTheDocument();
    
    const tabs = screen.getByText('NIMAS Packages').closest('.nav-tabs');
    expect(tabs).toBeInTheDocument();
  });

  it('displays the correct page title', () => {
    render(<App />);
    
    // Check that the main heading is properly styled
    const mainHeading = screen.getByText('NIMAS2PDF Web Application');
    expect(mainHeading).toHaveClass('text-center', 'mb-4');
  });

  it('shows the description text with proper styling', () => {
    render(<App />);
    
    const description = screen.getByText(/Upload, manage, and convert NIMAS packages to accessible PDF format/);
    expect(description).toHaveClass('text-center', 'text-muted', 'mb-5');
  });

  it('renders the success alert with proper styling', () => {
    render(<App />);
    
    const successAlert = screen.getByText(/New!/).closest('.alert');
    expect(successAlert).toHaveClass('alert', 'alert-success', 'text-center');
  });
});
