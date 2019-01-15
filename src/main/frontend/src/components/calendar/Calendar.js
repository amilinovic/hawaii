import React, { Component } from 'react';
import { tableTd, tableTh, tableTr } from './CalendarTableElements';
import styled from 'styled-components';
import HolidayImg from '../../img/holiday.svg';

const Table = styled.table`
  width: 100%;
  border-collapse: separate;
  border-spacing: 0px;
`;

const Image = styled.img`
  width: 15px;
  height: 15px;
`;

class Calendar extends Component {
  createTableHeader = () => {
    let thRender = [];
    thRender.push(tableTh('default', { first: true }));
    for (let i = 0; i < 31; i++) {
      thRender.push(tableTh(i + 1, { header: true }, i + 1));
    }
    return thRender;
  };

  cellClickHandler = (monthName, clickedDay) => {
    const selectedMonth = this.props.calendar.table.find(
      month => month.name === monthName
    );
    const selectedDay = selectedMonth.days.map(
      day =>
        day && day.date === clickedDay
          ? { ...day, selected: !day.selected }
          : day
    );
    const mappedCalendar = this.props.calendar.table.map(
      month =>
        monthName === month.name ? { ...month, days: selectedDay } : month
    );

    this.props.selectDay({ ...this.props.calendar, table: mappedCalendar });
  };

  createTableRows = month => {
    const { name, days } = month;

    const tdRender = [];

    const clickHandler = day => ({
      click: () => this.cellClickHandler(name, day)
    });

    tdRender.push(tableTd(name, { monthName: true }, name.substring(0, 3)));

    days.map((day, index) => {
      const selected =
        day && (day.selected && !day.publicHoliday)
          ? { selected: true }
          : { selected: false };
      if (!day) {
        tdRender.push(tableTd(`${index + 1}.${name}`, { disabled: true }));
      } else if (day.today) {
        tdRender.push(
          tableTd(
            `${day.date}.${name}`,
            {
              today: true,
              ...clickHandler(day.date),
              ...selected
            },
            day.publicHoliday && <Image src={HolidayImg} />
          )
        );
      } else {
        tdRender.push(
          day.weekend
            ? tableTd(
                `${day.date}.${name}`,
                { weekend: true },
                day.publicHoliday && <Image src={HolidayImg} />
              )
            : tableTd(
                `${day.date}.${name}`,
                {
                  ...clickHandler(day.date),
                  ...selected
                },
                day.publicHoliday && <Image src={HolidayImg} />
              )
        );
      }
      return day;
    });

    return tdRender;
  };

  render() {
    return (
      <Table>
        <thead>
          {tableTr('monthNames', { headerRow: true }, this.createTableHeader())}
        </thead>
        <tbody>
          {this.props.calendar &&
            this.props.calendar.table.map(month =>
              tableTr(month.name, {}, this.createTableRows(month))
            )}
        </tbody>
      </Table>
    );
  }
}

export default Calendar;
