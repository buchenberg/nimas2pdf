import React, { useState, useCallback } from 'react';
import { Card, Button, Form, Alert, Spinner } from 'react-bootstrap';
import { useDropzone } from 'react-dropzone';
import axios from 'axios';

interface FileUploadProps {}

const FileUpload: React.FC<FileUploadProps> = () => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState<{ text: string; type: 'success' | 'danger' | 'info' } | null>(null);
  const [outputName, setOutputName] = useState('');

  const onDrop = useCallback((acceptedFiles: File[]) => {
    if (acceptedFiles.length > 0) {
      setSelectedFile(acceptedFiles[0]);
      setMessage({ text: `File selected: ${acceptedFiles[0].name}`, type: 'info' });
    }
  }, []);

  const { getRootProps, getInputProps, isDragActive } = useDropzone({
    onDrop,
    accept: {
      'application/xml': ['.xml'],
      'text/xml': ['.xml'],
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
    if (outputName.trim()) {
      formData.append('outputName', outputName.trim());
    }

    try {
      const response = await axios.post('/api/convert', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      if (response.data.success) {
        setMessage({
          text: `Conversion started! ID: ${response.data.conversionId}`,
          type: 'success'
        });
        setSelectedFile(null);
        setOutputName('');
      } else {
        setMessage({
          text: response.data.message || 'Conversion failed',
          type: 'danger'
        });
      }
    } catch (error: any) {
      setMessage({
        text: error.response?.data?.message || 'Upload failed',
        type: 'danger'
      });
    } finally {
      setUploading(false);
    }
  };

  return (
    <Card className="status-card mb-4">
      <Card.Header>
        <h5 className="mb-0">
          <i className="bi bi-cloud-upload me-2"></i>
          Upload NIMAS File
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
              Drop the file here...
            </p>
          ) : (
            <div>
              <i className="bi bi-cloud-upload display-4 text-primary mb-3"></i>
              <p className="mb-2">
                <strong>Drag & drop a NIMAS file here</strong>
              </p>
              <p className="text-muted mb-0">
                or click to browse files
              </p>
              <small className="text-muted">
                Supports: .xml, .zip files
              </small>
            </div>
          )}
        </div>

        {selectedFile && (
          <div className="file-info mt-3">
            <h6>Selected File:</h6>
            <p className="mb-2">
              <strong>{selectedFile.name}</strong>
              <br />
              <small className="text-muted">
                Size: {(selectedFile.size / 1024 / 1024).toFixed(2)} MB
              </small>
            </p>
            
            <Form.Group className="mb-3">
              <Form.Label>Output Filename (optional):</Form.Label>
              <Form.Control
                type="text"
                placeholder="e.g., my-document.pdf"
                value={outputName}
                onChange={(e) => setOutputName(e.target.value)}
              />
              <Form.Text className="text-muted">
                Leave empty to use auto-generated name
              </Form.Text>
            </Form.Group>

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
                  Converting...
                </>
              ) : (
                <>
                  <i className="bi bi-play-circle me-2"></i>
                  Start Conversion
                </>
              )}
            </Button>
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

export default FileUpload;
