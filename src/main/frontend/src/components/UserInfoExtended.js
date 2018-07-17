import React, { Component } from 'react';
import { Col } from 'reactstrap';
import { UserImage } from './common/UserImage';

export default class UserInfoComponent extends Component {
  render() {
    return (
      <Col className="d-flex align-items-center">
        <UserImage image={this.props.employeeInfo.picture.large} size="100px" />
        <div>
          <h3>{this.props.employeeInfo.name.first}</h3>
          <h5>Web developer</h5>
          <h5>{this.props.employeeInfo.email}</h5>
        </div>
      </Col>
    );
  }
}
