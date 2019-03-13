import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

export default class PublicHolidayItem extends Component {
  render() {
    const {
      publicHoliday: { id, name, date }
    } = this.props;
    return (
      <tr>
        <td>
          <NavLink to={`public-holidays/${id}`}>{name}</NavLink>
        </td>
        <td>{date}</td>
      </tr>
    );
  }
}
