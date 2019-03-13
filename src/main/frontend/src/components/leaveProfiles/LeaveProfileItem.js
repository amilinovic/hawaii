import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class LeaveProfileItem extends Component {
  render() {
    const {
      leaveProfile: { id, name }
    } = this.props;
    return (
      <tr>
        <td>
          <NavLink to={`leave-profiles/${id}`}>{name}</NavLink>
        </td>
      </tr>
    );
  }
}

export default LeaveProfileItem;
