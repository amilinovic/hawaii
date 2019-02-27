import React from 'react';
import styled from 'styled-components';
import { ifProp, prop, switchProp } from 'styled-tools';
import HolidayImg from '../../img/holiday.svg';

// TODO: Handle overriding better with styled-components
const DayCell = styled.td`
  border: ${ifProp('isToday', '1px solid grey', '1px solid #c0c0c0')};
  border-style: ${ifProp('isToday', 'double')};
  min-width: 20px;
  font-size: 14px;
  border-color: ${ifProp('isToday', 'grey')};
  background-color: ${ifProp('disabled', '#9e9e9e')};
  background-color: ${ifProp('isWeekend', '#E0E0E0')};
  cursor: ${ifProp('disabled', 'no-drop')};
  &:hover {
    background-color: rgba(0, 0, 0, 0.5);
  }
  background-color: ${switchProp(prop('requestStatus'), {
    APPROVED: 'green',
    PENDING: 'orange'
  })};
  content: ${prop('requestStatus')};
`;

const Image = styled.img`
  width: 15px;
  height: 15px;
`;

export const Day = props => {
  return (
    <DayCell className="text-center align-middle" {...props}>
      {props.publicHoliday && <Image src={HolidayImg} alt="public_holiday" />}
      {props.personalDay && <Image src={props.personalDay.iconUrl} />}
    </DayCell>
  );
};
