import React, { Component } from 'react';
import Calendar from '../components/calendar/Calendar';
import styled from 'styled-components';
import {
  getCalendar,
  getPublicHolidays,
  getMyPersonalDays
} from '../store/selectors';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import {
  incrementYear,
  decrementYear,
  selectDay
} from '../store/actions/calendarActions';
import { requestPublicHolidays } from '../store/actions/publicHolidayActions';
import { requestMyPersonalDays } from '../store/actions/myPersonalDaysActions';

const ExecomCalendarContainer = styled.div`
  display: flex;
  height: 100%;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
`;

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

const CalendarContainer = styled.div`
  width: 100%;
  background-color: white;
  border-radius: 10px;
  border: 1px solid grey;
  padding: 20px;
`;

class ExecomCalendar extends Component {
  componentDidMount() {
    this.props.requestPublicHolidays();
    this.props.requestMyPersonalDays();
  }

  getPublicHolidays = async () => {
    if (
      !this.props.publicHolidays ||
      1 ||
      (this.props.publicHolidays && this.props.publicHolidays.length) < 1
    ) {
      await this.props.requestPublicHolidays();
      return this.props.publicHolidays;
    }
    return this.props.publicHolidays;
  };

  getMyPersonalDays = async () => {
    if (!this.props.myPersonalDays || this.props.myPersonalDays.length < 1) {
      await this.props.requestMyPersonalDays();
      return this.props.myPersonalDays;
    }
    return this.props.myPersonalDays;
  };

  render() {
    return (
      <ExecomCalendarContainer>
        <CalendarWrapper>
          {/* Change button styling to Button from '/common/button' */}
          <button
            style={{
              backgroundColor: '#fb4b4f',
              color: 'white',
              padding: 10,
              borderRadius: 5,
              alignSelf: 'flex-end'
            }}
          >
            + New Request
          </button>
          <YearSelection>
            <YearControlButton
              onClick={() =>
                this.props.decrementYear({
                  selectedYear:
                    this.props.calendar.selectedYear || this.props.calendar.year
                })
              }
            >
              {'<'}
            </YearControlButton>
            <p>
              {this.props.calendar.selectedYear || this.props.calendar.year}
            </p>
            <YearControlButton
              onClick={() =>
                this.props.incrementYear({
                  selectedYear:
                    this.props.calendar.selectedYear || this.props.calendar.year
                })
              }
            >
              {'>'}
            </YearControlButton>
          </YearSelection>
          <CalendarContainer>
            {this.props.calendar.table && (
              <Calendar
                calendar={this.props.calendar}
                selectDay={this.props.selectDay}
                publicHolidays={this.props.publicHolidays}
                myPersonalDays={this.props.myPersonalDays}
              />
            )}
          </CalendarContainer>
        </CalendarWrapper>
      </ExecomCalendarContainer>
    );
  }
}

const mapStateToProps = state => ({
  calendar: getCalendar(state),
  publicHolidays: getPublicHolidays(state),
  myPersonalDays: getMyPersonalDays(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      incrementYear,
      decrementYear,
      selectDay,
      requestPublicHolidays,
      requestMyPersonalDays
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ExecomCalendar);
