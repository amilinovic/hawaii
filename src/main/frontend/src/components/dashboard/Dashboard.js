import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { requestLeaveTypes } from '../../store/actions/leaveTypesActions';
import { toggleModal } from '../../store/actions/modalActions';
import { requestPersonalDays } from '../../store/actions/personalDaysActions';
import { requestPublicHolidays } from '../../store/actions/publicHolidaysActions';
import {
  getLeaveTypes,
  getPersonalDays,
  getPublicHolidays,
  getRequest,
  getAllowance
} from '../../store/selectors';
import CalendarContainer from '../calendar/CalendarContainer';
import withResetOnNavigate from '../HOC/withResetOnNavigate';
import Loading from '../loading/Loading';
import InfoCard from '../info-card/InfoCard';

class Dashboard extends Component {
  componentDidMount() {
    this.props.requestPublicHolidays();
    this.props.requestPersonalDays();
    this.props.requestLeaveTypes();
  }

  toggle = () => {
    this.props.toggleModal();
  };

  getCardInfoProps = (allowanceType, allowance) => {
    if (allowance !== null) {
      if (allowanceType === 'annual') {
        return [
          {
            id: 1,
            type: 'Approved',
            hours: allowance.takenAnnual
          },
          {
            id: 2,
            type: 'Pending',
            hours: allowance.pendingAnnual
          },
          {
            id: 3,
            type: 'Remaining',
            hours: allowance.annual
          }
        ];
      } else if (allowanceType === 'sickness') {
        return [
          {
            id: 1,
            type: 'Approved',
            hours: allowance.sickness
          }
        ];
      } else {
        return [
          {
            id: 1,
            type: 'Approved',
            hours: allowance.takenTraining
          },
          {
            id: 2,
            type: 'Pending',
            hours: allowance.pendingTraining
          },
          {
            id: 3,
            type: 'Remaining',
            hours: allowance.training
          }
        ];
      }
    }
  };

  render() {
    if (this.props.publicHolidays === null || this.props.personalDays === null)
      return <Loading />;

    return (
      <div className="d-flex flex-grow-1 flex-column">
        <div className="d-flex justify-content-between px-4 pt-4 align-items-center">
          <h1>My Leave</h1>
          <button className="btn btn-danger" onClick={this.toggle}>
            + New Request
          </button>
        </div>
        <CalendarContainer
          publicHolidays={this.props.publicHolidays}
          personalDays={this.props.personalDays}
        />
        <div className="px-4 d-flex flex-column container-fluid">
          <div className="row">
            <div className="col-4">
              <InfoCard
                title="Leave"
                allowance={this.getCardInfoProps(
                  'annual',
                  this.props.allowance
                )}
              />
            </div>
            <div className="col-4">
              <InfoCard
                title="Sickness"
                allowance={this.getCardInfoProps(
                  'sickness',
                  this.props.allowance
                )}
              />
            </div>
            <div className="col-4">
              <InfoCard
                title="Education & Training"
                allowance={this.getCardInfoProps(
                  'training',
                  this.props.allowance
                )}
              />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  publicHolidays: getPublicHolidays(state),
  personalDays: getPersonalDays(state),
  request: getRequest(state),
  leaveTypes: getLeaveTypes(state),
  allowance: getAllowance(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators(
    {
      requestPublicHolidays,
      requestPersonalDays,
      requestLeaveTypes,
      toggleModal
    },
    dispatch
  );

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(Dashboard));
