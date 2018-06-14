import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';

export default class InformationHeader extends Component {
  render() {
    // TODO change mocked data with actual data
    return (
      <Container fluid={true}>
        <Row>
          <Col xs="3">
            {/* Component with image in circle should come here once it's pr goes through */}
            <h3>Nikola Kavezic</h3>
            <h5>Web developer</h5>
            <h5>nkavezic@execom.eu</h5>
          </Col>
          <Col className="d-flex flex-column">
            <Row className="h-100">
              <Col>Team</Col>
            </Row>
            <Row className="h-100">
              <Col>Hr manager</Col>
            </Row>
          </Col>
        </Row>
      </Container>
    );
  }
}
