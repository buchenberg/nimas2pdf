import React, { useState, useEffect, useCallback } from 'react';
import { Modal, Button, Form, Row, Col, Alert } from 'react-bootstrap';
import axios from 'axios';

interface ConversionProperties {
  baseFontSize: string;
  tableFontSize: string;
  lineHeight: string;
  baseFontFamily: string;
  headerFontFamily: string;
  pageOrientation: string;
  pageWidth: string;
  pageHeight: string;
  bookmarkHeaders: boolean;
  bookmarkTables: boolean;
}

interface ConversionPropertiesModalProps {
  show: boolean;
  onHide: () => void;
  packageId: string;
  packageDbId: number;
  packageTitle: string;
  onSave: (properties: ConversionProperties) => Promise<void>;
}

const ConversionPropertiesModal: React.FC<ConversionPropertiesModalProps> = ({
  show,
  onHide,
  packageId,
  packageDbId,
  packageTitle,
  onSave
}) => {
  const [properties, setProperties] = useState<ConversionProperties>({
    baseFontSize: '18pt',
    tableFontSize: '18pt',
    lineHeight: '1.5em',
    baseFontFamily: 'DejaVu Sans',
    headerFontFamily: 'DejaVu Serif',
    pageOrientation: 'portrait',
    pageWidth: '8.5in',
    pageHeight: '11in',
    bookmarkHeaders: false,
    bookmarkTables: false
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const loadExistingProperties = useCallback(async () => {
    try {
      setError(null);
      const response = await axios.get(`/api/packages/${packageDbId}/properties`);
      setProperties(response.data);
    } catch (err: any) {
      console.error('Failed to load existing properties:', err);
      // If loading fails, keep default properties but show a warning
      setError('Failed to load existing properties. Using defaults.');
    }
  }, [packageDbId]);

  // Load existing properties when modal opens
  useEffect(() => {
    if (show && packageDbId) {
      loadExistingProperties();
    }
  }, [show, packageDbId, loadExistingProperties]);

  const handleInputChange = (field: keyof ConversionProperties, value: string | boolean) => {
    setProperties(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      // Save properties via API
      await axios.put(`/api/packages/${packageDbId}/properties`, properties);
      
      // Also call the onSave callback for any additional handling
      await onSave(properties);
      
      setSuccess('Conversion properties updated successfully!');
      setTimeout(() => {
        onHide();
      }, 1500);
    } catch (err: any) {
      setError(err.response?.data || 'Failed to update conversion properties');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setProperties({
      baseFontSize: '18pt',
      tableFontSize: '18pt',
      lineHeight: '1.5em',
      baseFontFamily: 'DejaVu Sans',
      headerFontFamily: 'DejaVu Serif',
      pageOrientation: 'portrait',
      pageWidth: '8.5in',
      pageHeight: '11in',
      bookmarkHeaders: false,
      bookmarkTables: false
    });
  };

  return (
    <Modal show={show} onHide={onHide} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>
          <i className="bi bi-gear me-2"></i>
          Conversion Properties
        </Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          <Alert variant="info" className="mb-3">
            <strong>Package:</strong> {packageTitle} ({packageId})
          </Alert>

          {error && (
            <Alert variant="danger" className="mb-3">
              {error}
            </Alert>
          )}

          {success && (
            <Alert variant="success" className="mb-3">
              {success}
            </Alert>
          )}

          <Row>
            <Col md={6}>
              <h6 className="mb-3">Font Settings</h6>
              
              <Form.Group className="mb-3">
                <Form.Label>Base Font Size</Form.Label>
                <Form.Control
                  type="text"
                  value={properties.baseFontSize}
                  onChange={(e) => handleInputChange('baseFontSize', e.target.value)}
                  placeholder="e.g., 18pt"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Table Font Size</Form.Label>
                <Form.Control
                  type="text"
                  value={properties.tableFontSize}
                  onChange={(e) => handleInputChange('tableFontSize', e.target.value)}
                  placeholder="e.g., 18pt"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Line Height</Form.Label>
                <Form.Control
                  type="text"
                  value={properties.lineHeight}
                  onChange={(e) => handleInputChange('lineHeight', e.target.value)}
                  placeholder="e.g., 1.5em"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Base Font Family</Form.Label>
                <Form.Control
                  type="text"
                  value={properties.baseFontFamily}
                  onChange={(e) => handleInputChange('baseFontFamily', e.target.value)}
                  placeholder="e.g., DejaVu Sans"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Header Font Family</Form.Label>
                <Form.Control
                  type="text"
                  value={properties.headerFontFamily}
                  onChange={(e) => handleInputChange('headerFontFamily', e.target.value)}
                  placeholder="e.g., DejaVu Serif"
                />
              </Form.Group>
            </Col>

            <Col md={6}>
              <h6 className="mb-3">Page Settings</h6>
              
              <Form.Group className="mb-3">
                <Form.Label>Page Orientation</Form.Label>
                <Form.Select
                  value={properties.pageOrientation}
                  onChange={(e) => handleInputChange('pageOrientation', e.target.value)}
                >
                  <option value="portrait">Portrait</option>
                  <option value="landscape">Landscape</option>
                </Form.Select>
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Page Width</Form.Label>
                <Form.Control
                  type="text"
                  value={properties.pageWidth}
                  onChange={(e) => handleInputChange('pageWidth', e.target.value)}
                  placeholder="e.g., 8.5in"
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Page Height</Form.Label>
                <Form.Control
                  type="text"
                  value={properties.pageHeight}
                  onChange={(e) => handleInputChange('pageHeight', e.target.value)}
                  placeholder="e.g., 11in"
                />
              </Form.Group>

              <h6 className="mb-3">Bookmark Settings</h6>
              
              <Form.Group className="mb-3">
                <Form.Check
                  type="checkbox"
                  label="Include Headers in Bookmarks"
                  checked={properties.bookmarkHeaders}
                  onChange={(e) => handleInputChange('bookmarkHeaders', e.target.checked)}
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Check
                  type="checkbox"
                  label="Include Tables in Bookmarks"
                  checked={properties.bookmarkTables}
                  onChange={(e) => handleInputChange('bookmarkTables', e.target.checked)}
                />
              </Form.Group>
            </Col>
          </Row>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="outline-secondary" onClick={handleReset} disabled={loading}>
            Reset to Defaults
          </Button>
          <Button variant="secondary" onClick={onHide} disabled={loading}>
            Cancel
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? (
              <>
                <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                Saving...
              </>
            ) : (
              'Save Properties'
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default ConversionPropertiesModal;
