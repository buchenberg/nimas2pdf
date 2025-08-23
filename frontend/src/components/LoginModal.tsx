import React, { useState, useEffect } from 'react';
import { Modal, Button, Row, Col, Card, Spinner } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';

interface LoginModalProps {
  show: boolean;
  onHide: () => void;
}

interface Provider {
  name: string;
  url: string;
  icon: string;
  color: string;
  displayName: string;
}

const LoginModal: React.FC<LoginModalProps> = ({ show, onHide }) => {
  const { login } = useAuth();
  const [providers, setProviders] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchProviders = async () => {
      try {
        const response = await fetch('/api/auth/providers');
        if (response.ok) {
          const data = await response.json();
          setProviders(data.providers);
        }
      } catch (error) {
        console.error('Failed to fetch providers:', error);
      }
    };

    if (show) {
      fetchProviders();
    }
  }, [show]);

  const handleLogin = (provider: string) => {
    setLoading(true);
    login(provider);
  };

  const getProviderConfig = (provider: string): Provider => {
    const configs: Record<string, Provider> = {
      google: {
        name: 'google',
        url: providers.google,
        icon: 'bi-google',
        color: '#db4437',
        displayName: 'Google'
      },
      github: {
        name: 'github',
        url: providers.github,
        icon: 'bi-github',
        color: '#333',
        displayName: 'GitHub'
      },
      microsoft: {
        name: 'microsoft',
        url: providers.microsoft,
        icon: 'bi-microsoft',
        color: '#0078d4',
        displayName: 'Microsoft'
      }
    };
    return configs[provider];
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          <i className="bi bi-shield-lock me-2"></i>
          Sign In to NIMAS2PDF
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="text-center mb-4">
          <p className="text-muted">
            Choose your preferred sign-in method to access NIMAS2PDF features
          </p>
        </div>

        {loading && (
          <div className="text-center mb-4">
            <Spinner animation="border" role="status">
              <span className="visually-hidden">Signing in...</span>
            </Spinner>
            <p className="text-muted mt-2">Redirecting to authentication provider...</p>
          </div>
        )}

        <Row className="g-3">
          {Object.keys(providers).map((provider) => {
            const config = getProviderConfig(provider);
            if (!config) return null;

            return (
              <Col xs={12} key={provider}>
                <Card className="border-0 shadow-sm">
                  <Card.Body className="p-3">
                    <Button
                      variant="outline-secondary"
                      className="w-100 d-flex align-items-center justify-content-center"
                      onClick={() => handleLogin(provider)}
                      disabled={loading}
                      style={{ 
                        borderColor: config.color,
                        color: config.color
                      }}
                    >
                      <i className={`${config.icon} fs-5 me-3`}></i>
                      <span>Continue with {config.displayName}</span>
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            );
          })}
        </Row>

        {Object.keys(providers).length === 0 && !loading && (
          <div className="text-center">
            <i className="bi bi-exclamation-triangle text-warning fs-1"></i>
            <p className="text-muted mt-2">
              No authentication providers are configured.
              <br />
              Please check the server configuration.
            </p>
          </div>
        )}
      </Modal.Body>
      <Modal.Footer className="border-0">
        <small className="text-muted w-100 text-center">
          <i className="bi bi-shield-check me-1"></i>
          Secure OAuth2 authentication
        </small>
      </Modal.Footer>
    </Modal>
  );
};

export default LoginModal;
