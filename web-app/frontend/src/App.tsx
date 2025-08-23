import React from 'react';
import { Container, Row, Col, Alert } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import FileUpload from './components/FileUpload';
import ConversionStatus from './components/ConversionStatus';
import PropertiesPanel from './components/PropertiesPanel';
import './App.css';

function App() {
  return (
    <div className="App">
      <Container fluid className="py-4">
        <Row>
          <Col>
            <h1 className="text-center mb-4">
              <i className="bi bi-file-earmark-pdf text-danger me-2"></i>
              NIMAS2PDF Web Application
            </h1>
            <p className="text-center text-muted mb-5">
              Convert NIMAS files to accessible PDF format
            </p>
          </Col>
        </Row>

        <Row>
          <Col lg={8}>
            <FileUpload />
            <ConversionStatus />
          </Col>
          <Col lg={4}>
            <PropertiesPanel />
          </Col>
        </Row>

        <Row className="mt-4">
          <Col>
            <Alert variant="info" className="text-center">
              <i className="bi bi-info-circle me-2"></i>
              This web application provides the same conversion capabilities as the desktop version,
              but accessible from any web browser.
            </Alert>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default App;
