import React, { useState, useEffect } from 'react';
import { Card, Table, Badge, Button, Form, InputGroup, Spinner, Alert } from 'react-bootstrap';
import axios from 'axios';
import ConversionPropertiesModal from './ConversionPropertiesModal';

interface PackageListProps {
  onPackageSelect?: (packageId: string) => void;
  refreshTrigger?: number;
}

interface NimasPackage {
  id: number;
  packageId: string;
  title: string;
  creator?: string;
  language?: string;
  publisher?: string;
  format?: string;
  source?: string;
  subject?: string;
  status: string;
  uploadedAt: string;
}

const PackageList: React.FC<PackageListProps> = ({ onPackageSelect, refreshTrigger }) => {
  const [packages, setPackages] = useState<NimasPackage[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState<string>('ALL');
  const [error, setError] = useState<string | null>(null);
  const [showPropertiesModal, setShowPropertiesModal] = useState(false);
  const [selectedPackage, setSelectedPackage] = useState<NimasPackage | null>(null);

  useEffect(() => {
    loadPackages();
  }, [refreshTrigger]);

  const loadPackages = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const response = await axios.get('/api/packages');
      setPackages(response.data);
    } catch (error: any) {
      setError(error.response?.data || 'Failed to load packages');
    } finally {
      setLoading(false);
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'READY':
        return <Badge bg="success">Ready</Badge>;
      case 'UPLOADED':
        return <Badge bg="info">Uploaded</Badge>;
      case 'PROCESSING':
        return <Badge bg="warning">Processing</Badge>;
      case 'ERROR':
        return <Badge bg="danger">Error</Badge>;
      default:
        return <Badge bg="secondary">{status}</Badge>;
    }
  };

  const formatDateTime = (dateTime: string) => {
    return new Date(dateTime).toLocaleString();
  };

  const filteredPackages = packages.filter(pkg => {
    const matchesSearch = pkg.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         pkg.packageId.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         (pkg.creator && pkg.creator.toLowerCase().includes(searchTerm.toLowerCase())) ||
                         (pkg.subject && pkg.subject.toLowerCase().includes(searchTerm.toLowerCase()));
    
    const matchesStatus = statusFilter === 'ALL' || pkg.status === statusFilter;
    
    return matchesSearch && matchesStatus;
  });

  const handlePackageAction = (pkg: NimasPackage, action: string) => {
    switch (action) {
      case 'download':
        window.open(`/api/packages/${pkg.id}/download`, '_blank');
        break;
      case 'convert':
        if (onPackageSelect) {
          onPackageSelect(pkg.packageId);
        }
        break;
      case 'properties':
        setSelectedPackage(pkg);
        setShowPropertiesModal(true);
        break;
      case 'delete':
        if (window.confirm(`Are you sure you want to delete package "${pkg.title}"?`)) {
          deletePackage(pkg.id);
        }
        break;
    }
  };

  const deletePackage = async (packageId: number) => {
    try {
      await axios.delete(`/api/packages/${packageId}`);
      setMessage({ text: 'Package deleted successfully', type: 'success' });
      loadPackages(); // Refresh the list
    } catch (error: any) {
      setMessage({ text: error.response?.data || 'Failed to delete package', type: 'danger' });
    }
  };

  const handleSaveConversionProperties = async (properties: any) => {
    if (!selectedPackage) return;
    
    try {
      // The API call is now handled in the modal component
      // This callback can be used for additional processing if needed
      console.log('Properties saved for package:', selectedPackage.packageId, properties);
      
      // Show success message in the main component
      setMessage({ text: 'Conversion properties updated successfully!', type: 'success' });
    } catch (error: any) {
      throw error; // Re-throw to let the modal handle the error
    }
  };

  const [message, setMessage] = useState<{ text: string; type: 'success' | 'danger' } | null>(null);

  if (loading) {
    return (
      <Card className="status-card">
        <Card.Body className="text-center py-4">
          <Spinner animation="border" role="status">
            <span className="visually-hidden">Loading...</span>
          </Spinner>
          <p className="mt-2 mb-0">Loading packages...</p>
        </Card.Body>
      </Card>
    );
  }

  return (
    <Card className="status-card">
      <Card.Header>
        <h5 className="mb-0">
          <i className="bi bi-archive me-2"></i>
          NIMAS Packages
        </h5>
      </Card.Header>
      <Card.Body>
        {/* Search and Filter Controls */}
        <div className="row mb-3">
          <div className="col-md-6">
            <InputGroup>
              <InputGroup.Text>
                <i className="bi bi-search"></i>
              </InputGroup.Text>
              <Form.Control
                type="text"
                placeholder="Search packages..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
              />
            </InputGroup>
          </div>
          <div className="col-md-3">
            <Form.Select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
            >
              <option value="ALL">All Statuses</option>
              <option value="UPLOADED">Uploaded</option>
              <option value="READY">Ready</option>
              <option value="PROCESSING">Processing</option>
              <option value="ERROR">Error</option>
            </Form.Select>
          </div>
          <div className="col-md-3">
            <Button
              variant="outline-secondary"
              onClick={loadPackages}
              className="w-100"
            >
              <i className="bi bi-arrow-clockwise me-1"></i>
              Refresh
            </Button>
          </div>
        </div>

        {error && (
          <Alert variant="danger" className="mb-3">
            {error}
          </Alert>
        )}

        {message && (
          <Alert variant={message.type} className="mb-3">
            {message.text}
          </Alert>
        )}

        {filteredPackages.length === 0 ? (
          <div className="text-center py-4">
            <i className="bi bi-inbox display-4 text-muted d-block mb-3"></i>
            <p className="text-muted mb-0">
              {packages.length === 0 
                ? 'No packages uploaded yet. Upload a NIMAS package to get started!'
                : 'No packages match your search criteria.'
              }
            </p>
          </div>
        ) : (
          <div className="table-responsive">
            <Table striped hover>
              <thead>
                <tr>
                  <th>Package ID</th>
                  <th>Title</th>
                  <th>Creator</th>
                  <th>Subject</th>
                  <th>Status</th>
                  <th>Uploaded</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredPackages.map((pkg) => (
                  <tr key={pkg.id}>
                    <td>
                      <code className="small">{pkg.packageId}</code>
                    </td>
                    <td>
                      <strong>{pkg.title}</strong>
                      {pkg.format && (
                        <>
                          <br />
                          <small className="text-muted">{pkg.format}</small>
                        </>
                      )}
                    </td>
                    <td>{pkg.creator || 'N/A'}</td>
                    <td>{pkg.subject || 'N/A'}</td>
                    <td>{getStatusBadge(pkg.status)}</td>
                    <td>
                      <small className="text-muted">
                        {formatDateTime(pkg.uploadedAt)}
                      </small>
                    </td>
                    <td>
                      <div className="btn-group btn-group-sm" role="group">
                        <Button
                          variant="outline-primary"
                          size="sm"
                          onClick={() => handlePackageAction(pkg, 'properties')}
                          title="Conversion Properties"
                        >
                          <i className="bi bi-gear"></i>
                        </Button>
                        
                        <Button
                          variant="outline-success"
                          size="sm"
                          onClick={() => handlePackageAction(pkg, 'download')}
                          title="Download Package"
                        >
                          <i className="bi bi-download"></i>
                        </Button>
                        
                        {pkg.status === 'READY' && (
                          <Button
                            variant="outline-warning"
                            size="sm"
                            onClick={() => handlePackageAction(pkg, 'convert')}
                            title="Start Conversion"
                          >
                            <i className="bi bi-play-circle"></i>
                          </Button>
                        )}
                        
                        <Button
                          variant="outline-danger"
                          size="sm"
                          onClick={() => handlePackageAction(pkg, 'delete')}
                          title="Delete Package"
                        >
                          <i className="bi bi-trash"></i>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </div>
        )}

        <div className="mt-3 text-muted small">
          Showing {filteredPackages.length} of {packages.length} packages
        </div>
      </Card.Body>

      {/* Conversion Properties Modal */}
      {selectedPackage && (
        <ConversionPropertiesModal
          show={showPropertiesModal}
          onHide={() => {
            setShowPropertiesModal(false);
            setSelectedPackage(null);
          }}
          packageId={selectedPackage.packageId}
          packageDbId={selectedPackage.id}
          packageTitle={selectedPackage.title}
          onSave={handleSaveConversionProperties}
        />
      )}
    </Card>
  );
};

export default PackageList;
