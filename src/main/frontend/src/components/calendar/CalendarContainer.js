import moment from 'moment';
import React, { Component } from 'react';
import styled from 'styled-components';
import { createCalendar } from '../calendar/calendarUtils';
import YearlyCalendar from './YearlyCalendar';

const YearControlButton = styled.button`
  background-color: transparent;
  border: none;
  margin: 0px 10px 0px 10px;
  color: #fb4b4f;
  cursor: pointer;
  transition: 200ms ease;
  &:hover {
    transform: scale(1.5);
  }
`;

const CalendarWrapper = styled.div`
  width: 100%;
  padding: 20px;
  display: flex;
  flex-direction: column;
`;

const YearSelection = styled.div`
  width: 100%;
  background-color: lightgrey;
  border-radius: 10px;
  border: 1px solid grey;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  padding: 10px;
  color: #fb4b4f;
  margin: 20px 0px 20px 0px;
`;

const CalendarContainerBlock = styled.div`
  width: 100%;
  background-color: white;
  border-radius: 10px;
  border: 1px solid grey;
  padding: 20px;
`;
export default class CalendarContainer extends Component {
  state = {
    selectedYear: moment().year(),
    calendar: []
  };

  componentDidMount() {
    this.setState(prevState => {
      return {
        ...prevState,
        calendar: createCalendar(
          this.state.selectedYear,
          this.props.publicHolidays,
          this.props.personalDays
        )
      };
    });
  }

  handleYearChange = selectedYear => {
    this.setState(prevState => {
      return {
        ...prevState,
        selectedYear: selectedYear,
        calendar: createCalendar(
          selectedYear,
          this.props.publicHolidays,
          this.props.personalDays
        )
      };
    });
  };
  render() {
    if (!this.state.calendar.length || !this.props.publicHolidays) return null;
    return (
      <CalendarWrapper>
        <YearSelection>
          <YearControlButton
            onClick={() => this.handleYearChange(this.state.selectedYear - 1)}
          >
            {'<'}
          </YearControlButton>
          <p>{this.state.selectedYear}</p>
          <YearControlButton
            onClick={() => this.handleYearChange(this.state.selectedYear + 1)}
          >
            {'>'}
          </YearControlButton>
        </YearSelection>
        <CalendarContainerBlock>
          {this.state.calendar && (
            <YearlyCalendar
              calendar={this.state.calendar}
              selectDay={this.props.selectDay}
            />
          )}
        </CalendarContainerBlock>
      </CalendarWrapper>
    );
  }
}
