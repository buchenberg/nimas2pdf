import React, { useState } from 'react';
import { Dropdown, Badge, Modal, Button, Row, Col } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';

const UserProfile: React.FC = () => {
  const { user, logout } = useAuth();
  const [showProfile, setShowProfile] = useState(false);

  if (!user) return null;

  const getRoleBadgeVariant = (role: string) => {
    switch (role) {
      case 'ADMIN': return 'danger';
      case 'USER': return 'primary';
      case 'VIEWER': return 'secondary';
      default: return 'light';
    }
  };

  return (
    <>
      <Dropdown align="end">
        <Dropdown.Toggle variant="outline-secondary" id="user-dropdown" className="border-0">
          <div className="d-flex align-items-center">
            {user.pictureUrl ? (
              <img
                src={user.pictureUrl}
                alt={user.name}
                className="rounded-circle me-2"
                style={{ width: '24px', height: '24px' }}
              />
            ) : (
              <i className="bi bi-person-circle fs-5 me-2"></i>
            )}
            <span className="d-none d-md-inline">{user.name}</span>
          </div>
        </Dropdown.Toggle>

        <Dropdown.Menu>
          <Dropdown.Item onClick={() => setShowProfile(true)}>
            <i className="bi bi-person me-2"></i>
            Profile
          </Dropdown.Item>
          <Dropdown.Divider />
          <Dropdown.Item onClick={logout} className="text-danger">
            <i className="bi bi-box-arrow-right me-2"></i>
            Sign Out
          </Dropdown.Item>
        </Dropdown.Menu>
      </Dropdown>

      <Modal show={showProfile} onHide={() => setShowProfile(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>
            <i className="bi bi-person-circle me-2"></i>
            User Profile
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Row className="mb-4">
            <Col xs={12} className="text-center">
              {user.pictureUrl ? (
                <img
                  src={user.pictureUrl}
                  alt={user.name}
                  className="rounded-circle mb-3"
                  style={{ width: '80px', height: '80px' }}
                />
              ) : (
                <i className="bi bi-person-circle text-muted mb-3" style={{ fontSize: '80px' }}></i>
              )}
              <h5>{user.name}</h5>
              <p className="text-muted">{user.email}</p>
            </Col>
          </Row>

          <Row className="mb-3">
            <Col xs={4} className="text-muted">Provider:</Col>
            <Col xs={8}>
              <Badge bg="info" className="text-capitalize">
                <i className={`bi bi-${user.provider === 'google' ? 'google' : user.provider === 'github' ? 'github' : 'microsoft'} me-1`}></i>
                {user.provider}
              </Badge>
            </Col>
          </Row>

          <Row className="mb-3">
            <Col xs={4} className="text-muted">Roles:</Col>
            <Col xs={8}>
              {user.roles && user.roles.length > 0 ? (
                user.roles.map((role) => (
                  <Badge
                    key={role}
                    bg={getRoleBadgeVariant(role)}
                    className="me-1"
                  >
                    {role}
                  </Badge>
                ))
              ) : (
                <Badge bg="secondary">No roles assigned</Badge>
              )}
            </Col>
          </Row>

          <Row className="mb-3">
            <Col xs={4} className="text-muted">Status:</Col>
            <Col xs={8}>
              <Badge bg={user.status === 'ACTIVE' ? 'success' : 'warning'}>
                {user.status}
              </Badge>
            </Col>
          </Row>

          <Row>
            <Col xs={4} className="text-muted">User ID:</Col>
            <Col xs={8}>
              <code>{user.id}</code>
            </Col>
          </Row>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowProfile(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default UserProfile;
