import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import ConversionPropertiesModal from '../ConversionPropertiesModal';
import axios from 'axios';

// Mock axios
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

// Mock the onSave function
const mockOnSave = jest.fn();

const defaultProps = {
  show: true,
  onHide: jest.fn(),
  packageId: 'test-package-123',
  packageDbId: 1,
  packageTitle: 'Test Package',
  onSave: mockOnSave,
};

describe('ConversionPropertiesModal', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    
    // Mock axios.get to return default properties
    mockedAxios.get.mockResolvedValue({
      data: {
        baseFontSize: '18pt',
        tableFontSize: '18pt',
        lineHeight: '1.5em',
        baseFontFamily: 'DejaVu Sans',
        headerFontFamily: 'DejaVu Serif',
        pageOrientation: 'portrait',
        pageWidth: '8.5in',
        pageHeight: '11in',
        bookmarkHeaders: false,
        bookmarkTables: false,
      }
    });
    
    // Mock axios.put to return success
    mockedAxios.put.mockResolvedValue({ data: {} });
  });

  it('renders the modal when show is true', () => {
    render(<ConversionPropertiesModal {...defaultProps} />);
    
    expect(screen.getByText('Conversion Properties')).toBeInTheDocument();
    expect(screen.getByText(/Test Package/)).toBeInTheDocument();
    expect(screen.getByText(/test-package-123/)).toBeInTheDocument();
  });

  it('does not render when show is false', () => {
    render(<ConversionPropertiesModal {...defaultProps} show={false} />);
    
    expect(screen.queryByText('Conversion Properties')).not.toBeInTheDocument();
  });

  it('displays all form fields with default values', () => {
    render(<ConversionPropertiesModal {...defaultProps} />);
    
    // Check font settings - use getAllByDisplayValue since both baseFontSize and tableFontSize are 18pt
    const fontSizeInputs = screen.getAllByDisplayValue('18pt');
    expect(fontSizeInputs).toHaveLength(2); // baseFontSize and tableFontSize
    
    expect(screen.getByDisplayValue('1.5em')).toBeInTheDocument(); // lineHeight
    expect(screen.getByDisplayValue('DejaVu Sans')).toBeInTheDocument(); // baseFontFamily
    expect(screen.getByDisplayValue('DejaVu Serif')).toBeInTheDocument(); // headerFontFamily
    
    // Check page settings - use getByRole for select elements
    expect(screen.getByRole('combobox')).toBeInTheDocument(); // pageOrientation select
    expect(screen.getByDisplayValue('8.5in')).toBeInTheDocument(); // pageWidth
    expect(screen.getByDisplayValue('11in')).toBeInTheDocument(); // pageHeight
    
    // Check checkboxes
    const checkboxes = screen.getAllByRole('checkbox');
    expect(checkboxes).toHaveLength(2);
    expect(checkboxes[0]).not.toBeChecked();
    expect(checkboxes[1]).not.toBeChecked();
  });

  it('allows editing form fields', () => {
    render(<ConversionPropertiesModal {...defaultProps} />);
    
    // Get the first 18pt input (baseFontSize) by placeholder
    const baseFontSizeInputs = screen.getAllByDisplayValue('18pt');
    const baseFontSizeInput = baseFontSizeInputs[0]; // First one is baseFontSize
    fireEvent.change(baseFontSizeInput, { target: { value: '20pt' } });
    
    expect(screen.getByDisplayValue('20pt')).toBeInTheDocument();
  });

  it('calls API and onSave when form is submitted', async () => {
    mockOnSave.mockResolvedValue(undefined);
    
    render(<ConversionPropertiesModal {...defaultProps} />);
    
    // Wait for initial load
    await waitFor(() => {
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/packages/1/properties');
    });
    
    const saveButton = screen.getByText('Save Properties');
    fireEvent.click(saveButton);
    
    await waitFor(() => {
      expect(mockedAxios.put).toHaveBeenCalledWith('/api/packages/1/properties', {
        baseFontSize: '18pt',
        tableFontSize: '18pt',
        lineHeight: '1.5em',
        baseFontFamily: 'DejaVu Sans',
        headerFontFamily: 'DejaVu Serif',
        pageOrientation: 'portrait',
        pageWidth: '8.5in',
        pageHeight: '11in',
        bookmarkHeaders: false,
        bookmarkTables: false,
      });
      expect(mockOnSave).toHaveBeenCalled();
    });
  });

  it('resets form to defaults when reset button is clicked', () => {
    render(<ConversionPropertiesModal {...defaultProps} />);
    
    // Change a value
    const baseFontSizeInputs = screen.getAllByDisplayValue('18pt');
    const baseFontSizeInput = baseFontSizeInputs[0]; // First one is baseFontSize
    fireEvent.change(baseFontSizeInput, { target: { value: '20pt' } });
    
    // Click reset
    const resetButton = screen.getByText('Reset to Defaults');
    fireEvent.click(resetButton);
    
    // Check it's back to default - should have 2 inputs with 18pt again
    const fontSizeInputs = screen.getAllByDisplayValue('18pt');
    expect(fontSizeInputs).toHaveLength(2);
  });

  it('calls onHide when cancel button is clicked', () => {
    render(<ConversionPropertiesModal {...defaultProps} />);
    
    const cancelButton = screen.getByText('Cancel');
    fireEvent.click(cancelButton);
    
    expect(defaultProps.onHide).toHaveBeenCalled();
  });
});
