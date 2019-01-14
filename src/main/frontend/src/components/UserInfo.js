import React, { Component } from 'react';
import { UserImage } from './common/userImage';

export default class UserInfo extends Component {
  render() {
    return (
      <div className="d-inline-flex align-items-center">
        <UserImage />
        <div>
          <span>{this.props.employeeInfo.fullName}</span>
        </div>
      </div>
    );
  }
}
