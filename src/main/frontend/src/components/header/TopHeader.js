import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { UserImage } from '../UI/UserImage';

export default class TopHeader extends Component {
  render() {
    return (
      // TODO change mocked data with actual data
      <Container fluid>
        <Row className="justify-content-between align-items-end py-2">
          <Col xs="3">
            <img
              src="https://lh3.googleusercontent.com/Zeu3x2hWQxFtX6Mh8DYIgdqkkzN8EXI5C5regdIJm-lf0mCe2hKkQrs-kzSN7bTlY5IJbKuRI_N1Y5VLx6Z_0V51Ndk8fJ5oCIgmFg=h43"
              alt="hawaii logo"
            />
          </Col>
          <Col xs="3" className="text-center d-none d-lg-block">
            <img
              src="https://lh3.googleusercontent.com/Zeu3x2hWQxFtX6Mh8DYIgdqkkzN8EXI5C5regdIJm-lf0mCe2hKkQrs-kzSN7bTlY5IJbKuRI_N1Y5VLx6Z_0V51Ndk8fJ5oCIgmFg=h43"
              alt="execom logo"
            />
          </Col>
          <Col xs="3" className="justify-content-end d-none d-lg-inline-flex">
            <div className="d-inline-flex align-items-center">
              <UserImage />
              <span>Nikola Kavezic</span>
            </div>
          </Col>
        </Row>
      </Container>
    );
  }
}
