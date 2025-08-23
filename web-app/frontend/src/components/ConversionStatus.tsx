import React, { useState, useEffect } from 'react';
import { Card, ProgressBar, Button, Badge, ListGroup } from 'react-bootstrap';
import axios from 'axios';

interface ConversionStatusProps {}

interface ConversionItem {
  id: string;
  status: string;
  progress: number;
  message: string;
  startTime: string;
  lastUpdate: string;
  outputFileName?: string;
  fileSize?: number;
}

const ConversionStatus: React.FC<ConversionStatusProps> = () => {
  const [conversions, setConversions] = useState<ConversionItem[]>([]);
  const [polling, setPolling] = useState(false);

  // Mock data for demonstration - replace with real API calls
  useEffect(() => {
    // Simulate some conversion history
    setConversions([
      {
        id: 'demo-1',
        status: 'COMPLETED',
        progress: 100,
        message: 'Conversion completed successfully!',
        startTime: '2025-08-22T10:00:00',
        lastUpdate: '2025-08-22T10:02:30',
        outputFileName: 'document_1.pdf',
        fileSize: 2048576
      },
      {
        id: 'demo-2',
        status: 'PROCESSING',
        progress: 65,
        message: 'Generating PDF...',
        startTime: '2025-08-22T10:05:00',
        lastUpdate: '2025-08-22T10:06:45'
      }
    ]);
  }, []);

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

  const handleDownload = async (conversionId: string, fileName: string) => {
    try {
      const response = await axios.get(`/api/download/${conversionId}`, {
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

  return (
    <Card className="status-card">
      <Card.Header>
        <h5 className="mb-0">
          <i className="bi bi-list-check me-2"></i>
          Conversion Status
        </h5>
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

                <div className="row text-muted small">
                  <div className="col-6">
                    <i className="bi bi-clock me-1"></i>
                    Started: {formatDateTime(conversion.startTime)}
                  </div>
                  <div className="col-6">
                    <i className="bi bi-arrow-clockwise me-1"></i>
                    Updated: {formatDateTime(conversion.lastUpdate)}
                  </div>
                </div>

                {conversion.status === 'COMPLETED' && conversion.outputFileName && (
                  <div className="mt-3 p-3 bg-light rounded">
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <i className="bi bi-file-earmark-pdf text-danger me-2"></i>
                        <strong>{conversion.outputFileName}</strong>
                        <br />
                        <small className="text-muted">
                          Size: {formatFileSize(conversion.fileSize)}
                        </small>
                      </div>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        onClick={() => handleDownload(conversion.id, conversion.outputFileName!)}
                      >
                        <i className="bi bi-download me-1"></i>
                        Download
                      </Button>
                    </div>
                  </div>
                )}
              </ListGroup.Item>
            ))}
          </ListGroup>
        )}
      </Card.Body>
    </Card>
  );
};

export default ConversionStatus;
