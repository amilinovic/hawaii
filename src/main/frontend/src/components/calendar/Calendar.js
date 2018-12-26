import React, { Component } from 'react';
import { tableTd, tableTh, tableTr } from './CalendarTableElements';

class Calendar extends Component {
  createTableHeader = () => {
    let thRender = [];
    thRender.push(tableTh('default', { first: true }));
    for (let i = 0; i < 31; i++) {
      thRender.push(tableTh(i + 1, { header: true }, i + 1));
    }
    return thRender;
  };

  createTableRows = month => {
    const { name, days } = month;

    const tdRender = [];

    tdRender.push(tableTd(name, { monthName: true }, name.substring(0, 3)));

    days.map((day, index) => {
      if (!day) {
        tdRender.push(tableTd(`${index + 1}.${name}`, { disabled: true }));
      } else if (day.today) {
        tdRender.push(tableTd(`${day.date}.${name}`, { today: true }));
      } else {
        tdRender.push(
          day.weekend
            ? tableTd(`${day.date}.${name}`, { weekend: true })
            : tableTd(`${day.date}.${name}`)
        );
      }
      return day;
    });

    return tdRender;
  };

  render() {
    return (
      <table
        style={{
          borderCollapse: 'collapse',
          width: '100%'
        }}
      >
        <thead>
          {tableTr('monthNames', { headerRow: true }, this.createTableHeader())}
        </thead>
        <tbody>
          {this.props.calendar &&
            this.props.calendar.table.map(month =>
              tableTr(month.name, {}, this.createTableRows(month))
            )}
        </tbody>
      </table>
    );
  }
}

export default Calendar;
