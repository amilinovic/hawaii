import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import { UserImage } from '../common/UserImage';
import { ShadowRow } from '../common/ShadowRow';

export default class InformationHeader extends Component {
  render() {
    // TODO change mocked data with actual data
    return (
      <Container fluid>
        <ShadowRow className="py-2">
          <Col xl="4" className="d-flex align-items-center">
            <UserImage size="100px" />
            <div>
              <h3>Nikola Kavezic</h3>
              <h5>Web developer</h5>
              <h5>nkavezic@execom.eu</h5>
            </div>
          </Col>
          <Col className="col-8 flex-column d-none d-xl-flex">
            <Row className="h-100 align-items-center">
              <Col>Team</Col>
            </Row>
            <Row className="h-100 align-items-center">
              <Col>Hr manager</Col>
            </Row>
          </Col>
        </ShadowRow>
      </Container>
    );
  }
}
