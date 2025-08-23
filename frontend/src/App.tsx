import React, { useState } from 'react';
import { Container, Row, Col, Alert, Tab, Nav, Button, Navbar } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import PackageUpload from './components/PackageUpload';
import PackageList from './components/PackageList';
import ConversionStatus from './components/ConversionStatus';
import LoginModal from './components/LoginModal';
import UserProfile from './components/UserProfile';
import { AuthProvider, useAuth } from './context/AuthContext';
import './App.css';

const AppContent: React.FC = () => {
  const { isAuthenticated, isLoading } = useAuth();
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [activeTab, setActiveTab] = useState('upload');
  const [showLogin, setShowLogin] = useState(false);

  const handlePackageUploaded = (packageId: string) => {
    // Refresh the package list when a new package is uploaded
    setRefreshTrigger(prev => prev + 1);
    // Navigate to the NIMAS Packages tab after successful upload
    setActiveTab('packages');
  };

  const handleNavigateToConversions = () => {
    setActiveTab('conversions');
  };

  if (isLoading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
        <div className="text-center">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="text-muted mt-2">Loading...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return (
      <div className="App">
        <Container fluid className="py-4">
          {/* Header */}
          <Navbar bg="light" className="mb-4 rounded">
            <Container>
              <Navbar.Brand>
                <i className="bi bi-archive text-primary me-2"></i>
                NIMAS2PDF
              </Navbar.Brand>
              <Button variant="primary" onClick={() => setShowLogin(true)}>
                <i className="bi bi-box-arrow-in-right me-2"></i>
                Sign In
              </Button>
            </Container>
          </Navbar>

          {/* Welcome Content */}
          <Row className="justify-content-center">
            <Col lg={8}>
              <div className="text-center">
                <i className="bi bi-archive text-primary mb-4" style={{ fontSize: '4rem' }}></i>
                <h1 className="mb-4">Welcome to NIMAS2PDF</h1>
                <p className="lead text-muted mb-4">
                  Convert NIMAS packages to accessible PDF format with enterprise-grade security
                </p>
                
                <div className="row g-4 mb-5">
                  <div className="col-md-4">
                    <i className="bi bi-shield-check text-success fs-1"></i>
                    <h5 className="mt-2">Secure Authentication</h5>
                    <p className="text-muted">OAuth2/OIDC integration with Google, GitHub, and Microsoft</p>
                  </div>
                  <div className="col-md-4">
                    <i className="bi bi-cloud-upload text-primary fs-1"></i>
                    <h5 className="mt-2">Easy Upload</h5>
                    <p className="text-muted">Simple drag-and-drop interface for NIMAS packages</p>
                  </div>
                  <div className="col-md-4">
                    <i className="bi bi-file-pdf text-danger fs-1"></i>
                    <h5 className="mt-2">Accessible PDFs</h5>
                    <p className="text-muted">Generate fully accessible PDF documents</p>
                  </div>
                </div>

                <Button variant="primary" size="lg" onClick={() => setShowLogin(true)}>
                  <i className="bi bi-box-arrow-in-right me-2"></i>
                  Get Started - Sign In
                </Button>
              </div>
            </Col>
          </Row>

          <LoginModal show={showLogin} onHide={() => setShowLogin(false)} />
        </Container>
      </div>
    );
  }

  return (
    <div className="App">
      <Container fluid className="py-4">
        {/* Header with User Profile */}
        <Navbar bg="light" className="mb-4 rounded">
          <Container>
            <Navbar.Brand>
              <i className="bi bi-archive text-primary me-2"></i>
              NIMAS2PDF
            </Navbar.Brand>
            <UserProfile />
          </Container>
        </Navbar>

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

        <Tab.Container id="main-tabs" activeKey={activeTab} onSelect={(k) => setActiveTab(k || 'upload')}>
          <Row>
            <Col>
              <Nav variant="tabs" className="mb-4">
                <Nav.Item>
                  <Nav.Link eventKey="upload">
                    <i className="bi bi-cloud-upload me-2"></i>
                    Upload
                  </Nav.Link>
                </Nav.Item>
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
              </Nav>
            </Col>
          </Row>

          <Tab.Content>
            <Tab.Pane eventKey="upload">
              <Row>
                <Col lg={8}>
                  <PackageUpload onPackageUploaded={handlePackageUploaded} />
                </Col>
                <Col lg={4}>
                  <Alert variant="info">
                    <h6><i className="bi bi-info-circle me-2"></i>Upload NIMAS Package</h6>
                    <p className="mb-0 small">
                      Upload a NIMAS ZIP package to begin the conversion process. 
                      The package should contain XML content, images, and metadata for creating accessible educational materials.
                    </p>
                  </Alert>
                </Col>
              </Row>
            </Tab.Pane>
            
            <Tab.Pane eventKey="packages">
              <Row>
                <Col lg={8}>
                  <PackageList refreshTrigger={refreshTrigger} onNavigateToConversions={handleNavigateToConversions} />
                </Col>
                <Col lg={4}>
                  <Alert variant="info">
                    <h6><i className="bi bi-info-circle me-2"></i>About NIMAS Packages</h6>
                    <p className="mb-2 small">
                      View and manage your uploaded NIMAS packages. 
                      Start conversion jobs and monitor their progress.
                    </p>
                    <p className="mb-0 small">
                      <strong>Tip:</strong> After uploading a package, you can start a conversion job to generate an accessible PDF.
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
};

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
