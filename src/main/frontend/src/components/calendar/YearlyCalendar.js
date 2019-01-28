import React, { Component } from 'react';
import { tableCell, tableHeading, tableRow } from './CalendarTableElements';
import styled from 'styled-components';
import HolidayImg from '../../img/holiday.svg';

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  border-spacing: 0px;
`;

const Image = styled.img`
  width: 15px;
  height: 15px;
`;

class YearlyCalendar extends Component {
  createTableHeader = () => {
    let headerRow = [];
    headerRow.push(tableHeading('default', { first: true }));
    for (let i = 0; i < 31; i++) {
      headerRow.push(tableHeading(i + 1, { header: true }, i + 1));
    }
    return headerRow;
  };

  cellClickHandler = (monthName, clickedDay) => {
    const selectedMonth = this.props.calendar.table.find(
      month => month.name === monthName
    );

    const selectedMonthDays =
      selectedMonth.days &&
      selectedMonth.days.map(
        day =>
          day && day.date === clickedDay
            ? { ...day, selected: !day.selected }
            : day
      );
    const mappedCalendar = this.props.calendar.table.map(
      month =>
        monthName === month.name ? { ...month, days: selectedMonthDays } : month
    );

    this.props.selectDay({ ...this.props.calendar, table: mappedCalendar });
  };

  createTableRows = month => {
    const { name, days } = month;

    const monthRow = [];

    const clickHandler = day => () => this.cellClickHandler(name, day);

    monthRow.push(tableCell(name, { monthName: true }, name.substring(0, 3)));

    days.map((day, index) => {
      if (!day) {
        return monthRow.push(
          tableCell(`${index + 1}.${name}`, { disabled: true })
        );
      }

      const selected =
        day.selected && !day.publicHoliday
          ? { selected: true }
          : { selected: false };
      if (day.today) {
        monthRow.push(
          tableCell(
            `${day.date}.${name}`,
            {
              today: true,
              click: clickHandler(day.date),
              ...selected
            },
            <React.Fragment>
              {day.publicHoliday && <Image src={HolidayImg} />}
              {!day.publicHoliday &&
                day.personalDay && <Image src={day.personalDay.icon_url} />}
            </React.Fragment>
          )
        );
      } else {
        monthRow.push(
          day.weekend
            ? tableCell(
                `${day.date}.${name}`,
                { weekend: true },
                day.publicHoliday && <Image src={HolidayImg} />
              )
            : tableCell(
                `${day.date}.${name}`,
                {
                  click: clickHandler(day.date),

                  ...selected,
                  requestStatus: day.personalDay
                    ? day.personalDay.requestStatus
                    : null,
                  absenceType: day.personalDay
                    ? day.personalDay.absenceType
                    : null,
                  absenceSybtype: day.personalDay
                    ? day.personalDay.absencesubtype
                    : null
                },
                <React.Fragment>
                  {day.publicHoliday && <Image src={HolidayImg} />}
                  {!day.publicHoliday &&
                    day.personalDay && <Image src={day.personalDay.iconUrl} />}
                </React.Fragment>
              )
        );
      }

      return day;
    });

    return monthRow;
  };

  render() {
    return (
      <Table>
        <thead>
          {tableRow(
            'monthNames',
            { headerRow: true },
            this.createTableHeader()
          )}
        </thead>
        <tbody>
          {this.props.calendar.table.map(month =>
            tableRow(month.name, {}, this.createTableRows(month))
          )}
        </tbody>
      </Table>
    );
  }
}

export default YearlyCalendar;
