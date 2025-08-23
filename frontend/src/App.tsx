import React, { useState } from 'react';
import { Container, Row, Col, Alert, Tab, Nav } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import PackageUpload from './components/PackageUpload';
import PackageList from './components/PackageList';
import ConversionStatus from './components/ConversionStatus';
import PropertiesPanel from './components/PropertiesPanel';
import './App.css';

function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [activeTab, setActiveTab] = useState('packages');

  const handlePackageUploaded = (packageId: string) => {
    // Refresh the package list when a new package is uploaded
    setRefreshTrigger(prev => prev + 1);
  };

  const handleNavigateToConversions = () => {
    setActiveTab('conversions');
  };

  return (
    <div className="App">
      <Container fluid className="py-4">
        <Row>
          <Col>
            <h1 className="text-center mb-4">
              <i className="bi bi-archive text-primary me-2"></i>
              NIMAS2PDF Web Application
            </h1>
            <p className="text-center text-muted mb-5">
              Upload, manage, and convert NIMAS packages to accessible PDF format
            </p>
          </Col>
        </Row>

        <Tab.Container id="main-tabs" activeKey={activeTab} onSelect={(k) => setActiveTab(k || 'packages')}>
          <Row>
            <Col>
              <Nav variant="tabs" className="mb-4">
                <Nav.Item>
                  <Nav.Link eventKey="packages">
                    <i className="bi bi-archive me-2"></i>
                    NIMAS Packages
                  </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                  <Nav.Link eventKey="conversions">
                    <i className="bi bi-list-check me-2"></i>
                    Conversion Jobs
                  </Nav.Link>
                </Nav.Item>
                <Nav.Item>
                  <Nav.Link eventKey="properties">
                    <i className="bi bi-gear me-2"></i>
                    Conversion Properties
                  </Nav.Link>
                </Nav.Item>
              </Nav>
            </Col>
          </Row>

          <Tab.Content>
            <Tab.Pane eventKey="packages">
              <Row>
                <Col lg={8}>
                  <PackageUpload onPackageUploaded={handlePackageUploaded} />
                  <PackageList refreshTrigger={refreshTrigger} onNavigateToConversions={handleNavigateToConversions} />
                </Col>
                <Col lg={4}>
                  <Alert variant="info">
                    <h6><i className="bi bi-info-circle me-2"></i>About NIMAS Packages</h6>
                    <p className="mb-0 small">
                      NIMAS packages contain XML content, images, and metadata for creating accessible educational materials. 
                      Upload a ZIP file containing the complete package structure.
                    </p>
                  </Alert>
                </Col>
              </Row>
            </Tab.Pane>

            <Tab.Pane eventKey="conversions">
              <Row>
                <Col lg={8}>
                  <ConversionStatus />
                </Col>
                <Col lg={4}>
                  <Alert variant="info">
                    <h6><i className="bi bi-info-circle me-2"></i>Conversion Status</h6>
                    <p className="mb-0 small">
                      Monitor the progress of your PDF conversion jobs. 
                      Download completed PDFs and view detailed status information.
                    </p>
                  </Alert>
                </Col>
              </Row>
            </Tab.Pane>

            <Tab.Pane eventKey="properties">
              <Row>
                <Col lg={8}>
                  <PropertiesPanel />
                </Col>
                <Col lg={4}>
                  <Alert variant="info">
                    <h6><i className="bi bi-info-circle me-2"></i>Conversion Settings</h6>
                    <p className="mb-0 small">
                      Configure how your NIMAS content is converted to PDF. 
                      Adjust image quality, page layout, and special content handling.
                    </p>
                  </Alert>
                </Col>
              </Row>
            </Tab.Pane>
          </Tab.Content>
        </Tab.Container>

        <Row className="mt-4">
          <Col>
            <Alert variant="success" className="text-center">
              <i className="bi bi-check-circle me-2"></i>
              <strong>New!</strong> Database-backed package management with persistent storage and enterprise features.
            </Alert>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default App;
