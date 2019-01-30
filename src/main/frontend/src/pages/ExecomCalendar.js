import React, { Component } from 'react';
import YearlyCalendar from '../components/calendar/YearlyCalendar';
import styled from 'styled-components';
import {
  getCalendar,
  getPublicHolidays,
  getMyPersonalDays,
  getRequest,
  getLeaveTypes
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
import Request from '../components/request/Request';
import { openRequestPopup } from '../store/actions/requestActions';
import { requestLeaveTypes } from '../store/actions/leaveTypesActions';
import Button from '../components/common/Button';
import {
  fillWithMonthsAndDays,
  filterExistingDays,
  convertPublicDatesToMoment
} from '../components/calendar/calendarUtils';

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
  state = {
    months: fillWithMonthsAndDays()
  };

  componentDidMount() {
    this.props.requestPublicHolidays();
    this.props.requestMyPersonalDays();
    this.props.requestLeaveTypes();
  }

  fetchPublicHolidayAndAddMetadata = () => {
    this.props.publicHolidays.length < 1 && this.props.requestPublicHolidays();
    this.setState({
      ...this.state,
      months: filterExistingDays(
        this.state.months,
        {
          publicHolidays: convertPublicDatesToMoment(this.props.publicHolidays)
        },
        2019
      )
    });
  };

  getMyPersonalDays = () => {
    this.props.requestMyPersonalDays();
    return this.props.myPersonalDays;
  };

  render() {
    return (
      <ExecomCalendarContainer>
        <CalendarWrapper>
          <Button
            align="flex-end"
            click={() => this.props.openRequestPopup()}
            title="+ New Request"
          />
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
            <YearlyCalendar
              calendar={this.state.months}
              selectDay={this.props.selectDay}
              // publicHolidays={this.props.publicHolidays}
              publicHolidays={this.fetchPublicHolidayAndAddMetadata}
              myPersonalDays={this.props.myPersonalDays}
            />
          </CalendarContainer>
        </CalendarWrapper>
        {this.props.request.openPopup && (
          <Request leaveTypes={this.props.leaveTypes} />
        )}
      </ExecomCalendarContainer>
    );
  }
}

const mapStateToProps = state => ({
  calendar: getCalendar(state),
  publicHolidays: getPublicHolidays(state),
  myPersonalDays: getMyPersonalDays(state),
  request: getRequest(state),
  leaveTypes: getLeaveTypes(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      incrementYear,
      decrementYear,
      selectDay,
      requestPublicHolidays,
      requestMyPersonalDays,
      openRequestPopup,
      requestLeaveTypes
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ExecomCalendar);
