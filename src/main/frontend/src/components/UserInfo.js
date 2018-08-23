import React, { Component } from 'react';
import { UserImage } from './common/userImage';

export default class UserInfo extends Component {
  render() {
    return (
      <div className="d-inline-flex align-items-center">
        <UserImage image={this.props.employeeInfo.picture.large} />
        <div>
          <span>{this.props.employeeInfo.name.first}</span>
        </div>
      </div>
    );
  }
}
