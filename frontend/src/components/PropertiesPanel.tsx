import React, { useState, useEffect } from 'react';
import { Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import axios from 'axios';

interface PropertiesPanelProps {}

interface ConversionProperties {
  imageDpi: number;
  maxImageWidth: number;
  maxImageHeight: number;
  pageSize: string;
  marginTop: number;
  marginBottom: number;
  marginLeft: number;
  marginRight: number;
  enableMathML: boolean;
  enableSVG: boolean;
}

const PropertiesPanel: React.FC<PropertiesPanelProps> = () => {
  const [properties, setProperties] = useState<ConversionProperties>({
    imageDpi: 300,
    maxImageWidth: 800,
    maxImageHeight: 600,
    pageSize: 'A4',
    marginTop: 20,
    marginBottom: 20,
    marginLeft: 20,
    marginRight: 20,
    enableMathML: true,
    enableSVG: true
  });

  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState<{ text: string; type: 'success' | 'danger' } | null>(null);

  useEffect(() => {
    loadProperties();
  }, []);

  const loadProperties = async () => {
    setLoading(true);
    try {
      const response = await axios.get('/api/properties');
      if (response.data) {
        setProperties(response.data);
      }
    } catch (error) {
      console.error('Failed to load properties:', error);
      // Use default properties if loading fails
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    setSaving(true);
    setMessage(null);

    try {
      await axios.post('/api/properties', properties);
      setMessage({
        text: 'Properties saved successfully!',
        type: 'success'
      });
    } catch (error: any) {
      setMessage({
        text: error.response?.data?.message || 'Failed to save properties',
        type: 'danger'
      });
    } finally {
      setSaving(false);
    }
  };

  const handleReset = () => {
    setProperties({
      imageDpi: 300,
      maxImageWidth: 800,
      maxImageHeight: 600,
      pageSize: 'A4',
      marginTop: 20,
      marginBottom: 20,
      marginLeft: 20,
      marginRight: 20,
      enableMathML: true,
      enableSVG: true
    });
    setMessage(null);
  };

  const handleInputChange = (field: keyof ConversionProperties, value: any) => {
    setProperties(prev => ({
      ...prev,
      [field]: value
    }));
  };

  if (loading) {
    return (
      <Card className="properties-panel">
        <Card.Body className="text-center py-4">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
          <p className="mt-2 mb-0">Loading properties...</p>
        </Card.Body>
      </Card>
    );
  }

  return (
    <Card className="properties-panel">
      <Card.Header>
        <h5 className="mb-0">
          <i className="bi bi-gear me-2"></i>
          Conversion Properties
        </h5>
      </Card.Header>
      <Card.Body>
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Image DPI</Form.Label>
            <Form.Control
              type="number"
              min="72"
              max="600"
              value={properties.imageDpi}
              onChange={(e) => handleInputChange('imageDpi', parseInt(e.target.value))}
            />
            <Form.Text className="text-muted">
              Resolution for image processing (72-600)
            </Form.Text>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Max Image Width (px)</Form.Label>
            <Form.Control
              type="number"
              min="100"
              max="2000"
              value={properties.maxImageWidth}
              onChange={(e) => handleInputChange('maxImageWidth', parseInt(e.target.value))}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Max Image Height (px)</Form.Label>
            <Form.Control
              type="number"
              min="100"
              max="2000"
              value={properties.maxImageHeight}
              onChange={(e) => handleInputChange('maxImageHeight', parseInt(e.target.value))}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Page Size</Form.Label>
            <Form.Select
              value={properties.pageSize}
              onChange={(e) => handleInputChange('pageSize', e.target.value)}
            >
              <option value="A4">A4</option>
              <option value="A3">A3</option>
              <option value="Letter">Letter</option>
              <option value="Legal">Legal</option>
            </Form.Select>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Margins (mm)</Form.Label>
            <div className="row">
              <div className="col-6">
                <Form.Control
                  type="number"
                  placeholder="Top"
                  value={properties.marginTop}
                  onChange={(e) => handleInputChange('marginTop', parseInt(e.target.value))}
                />
              </div>
              <div className="col-6">
                <Form.Control
                  type="number"
                  placeholder="Bottom"
                  value={properties.marginBottom}
                  onChange={(e) => handleInputChange('marginBottom', parseInt(e.target.value))}
                />
              </div>
            </div>
            <div className="row mt-2">
              <div className="col-6">
                <Form.Control
                  type="number"
                  placeholder="Left"
                  value={properties.marginLeft}
                  onChange={(e) => handleInputChange('marginLeft', parseInt(e.target.value))}
                />
              </div>
              <div className="col-6">
                <Form.Control
                  type="number"
                  placeholder="Right"
                  value={properties.marginRight}
                  onChange={(e) => handleInputChange('marginRight', parseInt(e.target.value))}
                />
              </div>
            </div>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Check
              type="checkbox"
              label="Enable MathML Support"
              checked={properties.enableMathML}
              onChange={(e) => handleInputChange('enableMathML', e.target.checked)}
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Check
              type="checkbox"
              label="Enable SVG Support"
              checked={properties.enableSVG}
              onChange={(e) => handleInputChange('enableSVG', e.target.checked)}
            />
          </Form.Group>

          <div className="d-grid gap-2">
            <Button
              variant="primary"
              onClick={handleSave}
              disabled={saving}
            >
              {saving ? (
                <>
                  <Spinner
                    as="span"
                    animation="border"
                    size="sm"
                    role="status"
                    aria-hidden="true"
                    className="me-2"
                  />
                  Saving...
                </>
              ) : (
                <>
                  <i className="bi bi-save me-2"></i>
                  Save Properties
                </>
              )}
            </Button>
            
            <Button
              variant="outline-secondary"
              onClick={handleReset}
            >
              <i className="bi bi-arrow-clockwise me-2"></i>
              Reset to Defaults
            </Button>
          </div>
        </Form>

        {message && (
          <Alert variant={message.type} className="mt-3">
            {message.text}
          </Alert>
        )}
      </Card.Body>
    </Card>
  );
};

export default PropertiesPanel;
