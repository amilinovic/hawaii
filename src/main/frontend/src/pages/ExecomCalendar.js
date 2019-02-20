import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { requestLeaveTypes } from '../store/actions/leaveTypesActions';
import { requestPersonalDays } from '../store/actions/personalDaysActions';
import { requestPublicHolidays } from '../store/actions/publicHolidayActions';
import {
  getLeaveTypes,
  getPersonalDays,
  getPublicHolidays,
  getRequest
} from '../store/selectors';

class ExecomCalendar extends Component {
  componentDidMount() {
    this.props.requestPublicHolidays();
    this.props.requestPersonalDays();
    this.props.requestLeaveTypes();
  }

  render() {
    return (
      <div className="d-flex h-100 align-items-center justify-content-center">
        Execom calendar
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
)(ExecomCalendar);
