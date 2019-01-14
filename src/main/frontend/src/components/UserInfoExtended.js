import React, { Component } from 'react';
import { Col } from 'reactstrap';
import { UserImage } from './common/userImage';

export default class UserInfoComponent extends Component {
  render() {
    return (
      <Col className="d-flex align-items-center">
        <UserImage
          image={'/users/image/' + this.props.employeeInfo.id}
          size="100px"
        />
        <div>
          <h3>{this.props.employeeInfo.fullName}</h3>
          <h5>Web developer</h5>
          <h5>{this.props.employeeInfo.email}</h5>
        </div>
      </Col>
    );
  }
}
