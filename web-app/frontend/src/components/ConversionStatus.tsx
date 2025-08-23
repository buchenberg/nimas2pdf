import React, { useState, useEffect } from 'react';
import { Card, ProgressBar, Button, Badge, ListGroup } from 'react-bootstrap';
import axios from 'axios';

interface ConversionStatusProps {}

interface ConversionItem {
  id: number;
  jobId: string;
  status: string;
  progress: number;
  message: string;
  startedAt?: string;
  updatedAt: string;
  createdAt: string;
  completedAt?: string;
  errorMessage?: string;
  outputFilename?: string;
  outputSize?: number;
  nimasPackage?: {
    id: number;
    packageId: string;
    title: string;
  };
}

const ConversionStatus: React.FC<ConversionStatusProps> = () => {
  const [conversions, setConversions] = useState<ConversionItem[]>([]);

  // Load conversion jobs from API
  useEffect(() => {
    loadConversions();
    
    // Auto-refresh every 10 seconds
    const interval = setInterval(loadConversions, 10000);
    
    return () => clearInterval(interval);
  }, []);

  const loadConversions = async () => {
    try {
      const response = await axios.get('/api/conversion-jobs');
      setConversions(response.data);
    } catch (error) {
      console.error('Failed to load conversions:', error);
      // Set empty array on error
      setConversions([]);
    }
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'COMPLETED':
        return <Badge bg="success">Completed</Badge>;
      case 'PROCESSING':
        return <Badge bg="warning">Processing</Badge>;
      case 'FAILED':
        return <Badge bg="danger">Failed</Badge>;
      case 'PENDING':
        return <Badge bg="secondary">Pending</Badge>;
      default:
        return <Badge bg="secondary">{status}</Badge>;
    }
  };

  const getStatusIcon = (status: string) => {
    switch (status) {
      case 'COMPLETED':
        return 'bi-check-circle-fill text-success';
      case 'PROCESSING':
        return 'bi-arrow-clockwise text-warning';
      case 'FAILED':
        return 'bi-x-circle-fill text-danger';
      case 'PENDING':
        return 'bi-clock text-secondary';
      default:
        return 'bi-question-circle text-secondary';
    }
  };

  const handleDownload = async (jobId: string, fileName: string) => {
    try {
      const response = await axios.get(`/api/conversion-jobs/${jobId}/download`, {
        responseType: 'blob'
      });

      // Create download link
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', fileName);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Download failed:', error);
      alert('Download failed. Please try again.');
    }
  };

  const formatFileSize = (bytes?: number) => {
    if (!bytes) return 'Unknown';
    const mb = bytes / 1024 / 1024;
    return `${mb.toFixed(2)} MB`;
  };

  const formatDateTime = (dateTime: string) => {
    return new Date(dateTime).toLocaleString();
  };

  const handleCancelJob = async (jobId: string) => {
    try {
      await axios.post(`/api/conversion-jobs/${jobId}/cancel`);
      // Reload conversions to get updated status
      loadConversions();
    } catch (error: any) {
      console.error('Failed to cancel job:', error);
      alert(error.response?.data || 'Failed to cancel job');
    }
  };

  const handleRetryJob = async (jobId: string) => {
    try {
      await axios.post(`/api/conversion-jobs/${jobId}/retry`);
      // Reload conversions to get updated status
      loadConversions();
    } catch (error: any) {
      console.error('Failed to retry job:', error);
      alert(error.response?.data || 'Failed to retry job');
    }
  };

  return (
    <Card className="status-card">
      <Card.Header>
        <div className="d-flex justify-content-between align-items-center">
          <h5 className="mb-0">
            <i className="bi bi-list-check me-2"></i>
            Conversion Status
          </h5>
          <Button
            variant="outline-secondary"
            size="sm"
            onClick={loadConversions}
            title="Refresh conversion jobs"
          >
            <i className="bi bi-arrow-clockwise"></i>
          </Button>
        </div>
      </Card.Header>
      <Card.Body>
        {conversions.length === 0 ? (
          <p className="text-muted text-center py-4">
            <i className="bi bi-inbox display-4 d-block mb-3"></i>
            No conversions yet. Upload a file to get started!
          </p>
        ) : (
          <ListGroup variant="flush">
            {conversions.map((conversion) => (
              <ListGroup.Item
                key={conversion.id}
                className={`conversion-item ${conversion.status.toLowerCase()}`}
              >
                <div className="d-flex justify-content-between align-items-start mb-2">
                  <div>
                    <i className={`bi ${getStatusIcon(conversion.status)} me-2`}></i>
                    <strong>ID: {conversion.id}</strong>
                  </div>
                  {getStatusBadge(conversion.status)}
                </div>

                <p className="mb-2">{conversion.message}</p>

                {conversion.status === 'PROCESSING' && (
                  <ProgressBar
                    now={conversion.progress}
                    label={`${conversion.progress}%`}
                    className="progress-bar mb-2"
                  />
                )}

                {conversion.nimasPackage && (
                  <div className="mb-2">
                    <small className="text-muted">
                      <i className="bi bi-archive me-1"></i>
                      Package: {conversion.nimasPackage.packageId} - {conversion.nimasPackage.title}
                    </small>
                  </div>
                )}

                <div className="row text-muted small">
                  <div className="col-6">
                    <i className="bi bi-clock me-1"></i>
                    Created: {formatDateTime(conversion.createdAt)}
                  </div>
                  <div className="col-6">
                    <i className="bi bi-arrow-clockwise me-1"></i>
                    Updated: {formatDateTime(conversion.updatedAt)}
                  </div>
                </div>
                
                {conversion.startedAt && (
                  <div className="row text-muted small">
                    <div className="col-6">
                      <i className="bi bi-play-circle me-1"></i>
                      Started: {formatDateTime(conversion.startedAt)}
                    </div>
                    {conversion.completedAt && (
                      <div className="col-6">
                        <i className="bi bi-check-circle me-1"></i>
                        Completed: {formatDateTime(conversion.completedAt)}
                      </div>
                    )}
                  </div>
                )}
                
                {conversion.errorMessage && (
                  <div className="mt-2 p-2 bg-danger bg-opacity-10 rounded">
                    <small className="text-danger">
                      <i className="bi bi-exclamation-triangle me-1"></i>
                      Error: {conversion.errorMessage}
                    </small>
                  </div>
                )}

                {conversion.status === 'COMPLETED' && conversion.outputFilename && (
                  <div className="mt-3 p-3 bg-light rounded">
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <i className="bi bi-file-earmark-pdf text-danger me-2"></i>
                        <strong>{conversion.outputFilename}</strong>
                        <br />
                        <small className="text-muted">
                          Size: {formatFileSize(conversion.outputSize)}
                        </small>
                      </div>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        onClick={() => handleDownload(conversion.jobId, conversion.outputFilename!)}
                      >
                        <i className="bi bi-download me-1"></i>
                        Download
                      </Button>
                    </div>
                  </div>
                )}
                
                {/* Action Buttons */}
                <div className="mt-3 d-flex gap-2">
                  {conversion.status === 'PENDING' && (
                    <Button
                      variant="outline-danger"
                      size="sm"
                      onClick={() => handleCancelJob(conversion.jobId)}
                    >
                      <i className="bi bi-x-circle me-1"></i>
                      Cancel
                    </Button>
                  )}
                  
                  {conversion.status === 'PROCESSING' && (
                    <Button
                      variant="outline-danger"
                      size="sm"
                      onClick={() => handleCancelJob(conversion.jobId)}
                    >
                      <i className="bi bi-x-circle me-1"></i>
                      Cancel
                    </Button>
                  )}
                  
                  {conversion.status === 'FAILED' && (
                    <Button
                      variant="outline-warning"
                      size="sm"
                      onClick={() => handleRetryJob(conversion.jobId)}
                    >
                      <i className="bi bi-arrow-clockwise me-1"></i>
                      Retry
                    </Button>
                  )}
                </div>
              </ListGroup.Item>
            ))}
          </ListGroup>
        )}
      </Card.Body>
    </Card>
  );
};

export default ConversionStatus;
