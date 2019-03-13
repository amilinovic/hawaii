import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

export default class YearItem extends Component {
  render() {
    const {
      year: { yearId, year }
    } = this.props;

    return (
      <tr>
        <td>
          <NavLink to={`years/${yearId}`}>{year}</NavLink>
        </td>
      </tr>
    );
  }
}
