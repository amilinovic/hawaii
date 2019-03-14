import moment from 'moment';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import styled from 'styled-components';
import { requestAllowance } from '../../store/actions/allowanceActions';
import { createCalendar } from '../calendar/calendarUtils';
import YearlyCalendar from './YearlyCalendar';

const YearControlButton = styled.button`
  background-color: transparent;
  color: #fb4b4f;
  cursor: pointer;
  transition: 200ms ease;
  &:hover {
    transform: scale(1.5);
  }
`;

const YearSelection = styled.div`
  background-color: lightgrey;
  border: 1px solid grey;
  color: #fb4b4f;
`;

const CalendarContainerBlock = styled.div`
  border: 1px solid grey;
  background: white;
`;

class CalendarContainer extends Component {
  state = {
    selectedYear: moment().year(),
    calendar: createCalendar(
      moment().year(),
      this.props.publicHolidays,
      this.props.personalDays
    )
  };

  componentDidMount() {
    this.props.requestAllowance(this.state.selectedYear);
  }

  componentDidUpdate(prevProps) {
    if (prevProps !== this.props) {
      this.handleYearChange(this.state.selectedYear);
    }
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

    this.props.requestAllowance(selectedYear);
  };

  render() {
    return (
      <div className="px-4 d-flex flex-column">
        <YearSelection className="rounded d-flex align-items-center justify-content-center p-2 my-3">
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
        <CalendarContainerBlock className="p-3 rounded mb-5">
          {this.state.calendar && (
            <YearlyCalendar
              calendar={this.state.calendar}
              selectDay={this.props.selectDay}
            />
          )}
        </CalendarContainerBlock>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestAllowance
    },
    dispatch
  );

export default connect(
  null,
  mapDispatchToProps
)(CalendarContainer);
