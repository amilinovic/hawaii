import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { requestLeaveTypes } from '../../store/actions/leaveTypesActions';
import { requestPersonalDays } from '../../store/actions/personalDaysActions';
import { requestPublicHolidays } from '../../store/actions/publicHolidayActions';
import {
  getLeaveTypes,
  getPersonalDays,
  getPublicHolidays,
  getRequest
} from '../../store/selectors';
import CalendarContainer from '../calendar/CalendarContainer';
import withResetOnNavigate from '../HOC/withResetOnNavigate';
import Loading from '../loading/Loading';
import RequestModal from '../modal/RequestModal';

class Dashboard extends Component {
  componentDidMount() {
    this.props.requestPublicHolidays();
    this.props.requestPersonalDays();
    this.props.requestLeaveTypes();
  }

  render() {
    if (this.props.publicHolidays === null || this.props.personalDays === null)
      return <Loading />;

    return (
      <div className="d-flex flex-grow-1 justify-content-between flex-column">
        <div className="d-flex justify-content-between px-4 pt-4 align-items-center">
          <h1>My Leave</h1>
          <RequestModal />
        </div>
        <CalendarContainer
          publicHolidays={this.props.publicHolidays}
          personalDays={this.props.personalDays}
        />
      </div>
    );
  }
}

const mapStateToProps = state => ({
  publicHolidays: getPublicHolidays(state),
  personalDays: getPersonalDays(state),
  request: getRequest(state),
  leaveTypes: getLeaveTypes(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestPublicHolidays,
      requestPersonalDays,
      requestLeaveTypes
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(Dashboard));
