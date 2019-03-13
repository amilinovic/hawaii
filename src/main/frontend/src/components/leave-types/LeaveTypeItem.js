import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

class TeamItem extends Component {
  render() {
    const {
      leaveType: { id, name, absenceType, absenceSubtype, active }
    } = this.props;

    return (
      <tr>
        <td>
          <NavLink to={`leave-types/${id}`}>{name}</NavLink>
        </td>
        <td>{absenceType}</td>
        <td>{absenceSubtype}</td>
        <td>{active.toString()}</td>
      </tr>
    );
  }
}

export default TeamItem;
