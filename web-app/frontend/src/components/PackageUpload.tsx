import React, { useState, useCallback } from 'react';
import { Card, Button, Alert, Spinner, Badge } from 'react-bootstrap';
import { useDropzone } from 'react-dropzone';
import axios from 'axios';

interface PackageUploadProps {
  onPackageUploaded?: (packageId: string) => void;
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

const PackageUpload: React.FC<PackageUploadProps> = ({ onPackageUploaded }) => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState<{ text: string; type: 'success' | 'danger' | 'info' } | null>(null);
  const [uploadedPackage, setUploadedPackage] = useState<NimasPackage | null>(null);

  const onDrop = useCallback((acceptedFiles: File[]) => {
    if (acceptedFiles.length > 0) {
      const file = acceptedFiles[0];
      if (file.name.toLowerCase().endsWith('.zip')) {
        setSelectedFile(file);
        setMessage({ text: `NIMAS package selected: ${file.name}`, type: 'info' });
        setUploadedPackage(null);
      } else {
        setMessage({ text: 'Please select a ZIP file containing a NIMAS package', type: 'danger' });
      }
    }
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'application/zip': ['.zip'],
      'application/x-zip-compressed': ['.zip']
    },
    multiple: false
  });

  const handleUpload = async () => {
    if (!selectedFile) return;

    setUploading(true);
    setMessage(null);

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await axios.post('/api/packages/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      const packageData = response.data;
      setUploadedPackage(packageData);
      
      setMessage({
        text: `NIMAS package uploaded successfully! Package ID: ${packageData.packageId}`,
        type: 'success'
      });

      // Notify parent component
      if (onPackageUploaded) {
        onPackageUploaded(packageData.packageId);
      }

      // Reset file selection
      setSelectedFile(null);
    } catch (error: any) {
      setMessage({
        text: error.response?.data || 'Package upload failed',
        type: 'danger'
      });
    } finally {
      setUploading(false);
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

  return (
    <Card className="status-card mb-4">
      <Card.Header>
        <h5 className="mb-0">
          <i className="bi bi-archive me-2"></i>
          Upload NIMAS Package
        </h5>
      </Card.Header>
      <Card.Body>
        <div
          {...getRootProps()}
          className={`upload-zone ${isDragActive ? 'dragover' : ''}`}
        >
          <input {...getInputProps()} />
          {isDragActive ? (
            <p className="text-success mb-0">
              <i className="bi bi-cloud-arrow-down me-2"></i>
              Drop the NIMAS package here...
            </p>
          ) : (
            <div>
              <i className="bi bi-archive display-4 text-primary mb-3"></i>
              <p className="mb-2">
                <strong>Drag & drop a NIMAS ZIP package here</strong>
              </p>
              <p className="text-muted mb-0">
                or click to browse files
              </p>
              <small className="text-muted">
                Supports: .zip files containing NIMAS packages
              </small>
            </div>
          )}
        </div>

        {selectedFile && (
          <div className="file-info mt-3">
            <h6>Selected Package:</h6>
            <p className="mb-2">
              <strong>{selectedFile.name}</strong>
              <br />
              <small className="text-muted">
                Size: {(selectedFile.size / 1024 / 1024).toFixed(2)} MB
              </small>
            </p>

            <Button
              variant="primary"
              onClick={handleUpload}
              disabled={uploading}
              className="w-100"
            >
              {uploading ? (
                <>
                  <Spinner
                    as="span"
                    animation="border"
                    size="sm"
                    role="status"
                    aria-hidden="true"
                    className="me-2"
                  />
                  Uploading Package...
                </>
              ) : (
                <>
                  <i className="bi bi-upload me-2"></i>
                  Upload NIMAS Package
                </>
              )}
            </Button>
          </div>
        )}

        {uploadedPackage && (
          <div className="package-info mt-3 p-3 bg-light rounded">
            <h6 className="text-success">
              <i className="bi bi-check-circle me-2"></i>
              Package Uploaded Successfully!
            </h6>
            
            <div className="row">
              <div className="col-md-6">
                <p className="mb-1">
                  <strong>Package ID:</strong> {uploadedPackage.packageId}
                </p>
                <p className="mb-1">
                  <strong>Title:</strong> {uploadedPackage.title}
                </p>
                {uploadedPackage.creator && (
                  <p className="mb-1">
                    <strong>Creator:</strong> {uploadedPackage.creator}
                  </p>
                )}
                {uploadedPackage.subject && (
                  <p className="mb-1">
                    <strong>Subject:</strong> {uploadedPackage.subject}
                  </p>
                )}
              </div>
              <div className="col-md-6">
                <p className="mb-1">
                  <strong>Status:</strong> {getStatusBadge(uploadedPackage.status)}
                </p>
                <p className="mb-1">
                  <strong>Format:</strong> {uploadedPackage.format || 'N/A'}
                </p>
                <p className="mb-1">
                  <strong>Language:</strong> {uploadedPackage.language || 'N/A'}
                </p>
                <p className="mb-1">
                  <strong>Uploaded:</strong> {formatDateTime(uploadedPackage.uploadedAt)}
                </p>
              </div>
            </div>

            <div className="mt-3">
              <Button
                variant="outline-primary"
                size="sm"
                className="me-2"
                onClick={() => window.open(`/api/packages/${uploadedPackage.id}/download`, '_blank')}
              >
                <i className="bi bi-download me-1"></i>
                Download Package
              </Button>
              
              <Button
                variant="outline-success"
                size="sm"
                onClick={() => {
                  // TODO: Start conversion process
                  setMessage({ text: 'Starting conversion process...', type: 'info' });
                }}
              >
                <i className="bi bi-play-circle me-1"></i>
                Start Conversion
              </Button>
            </div>
          </div>
        )}

        {message && (
          <Alert variant={message.type} className="mt-3">
            {message.text}
          </Alert>
        )}
      </Card.Body>
    </Card>
  );
};

export default PackageUpload;
