import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  removeLeaveType,
  requestLeaveType
} from '../store/actions/leaveTypeActions';
import { getLeaveType } from '../store/selectors';

class LeaveType extends Component {
  componentDidMount() {
    this.props.requestLeaveType(this.props.match.params.id);
  }

  render() {
    if (!this.props.leaveType) return null;

    const {
      leaveType: { id, name, comment, active, absenceType, absenceSubtype }
    } = this.props;

    return (
      <div className="d-flex flex-grow-1 p-4 flex-column align-items-center">
        <h1>Absence name</h1>
        <h5 className="text-danger mb-3">{name}</h5>
        <h1>Type</h1>
        <h5 className="text-danger mb-3">{absenceType}</h5>
        <h1>Subtype</h1>
        <h5 className="text-danger mb-3">{absenceSubtype}</h5>
        <h1>Comment</h1>
        <h5 className="text-danger mb-3">{comment}</h5>
        <h1>Active</h1>
        <h5 className="text-danger mb-3">{active.toString()}</h5>
        <div>
          <button className="btn mr-3">
            <NavLink to={`/leave-types/${id}/edit`}>Edit</NavLink>
          </button>
          <button
            className="btn btn-danger"
            onClick={() => this.props.removeLeaveType({ id })}
          >
            Delete
          </button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  leaveType: getLeaveType(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestLeaveType, removeLeaveType }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(LeaveType));
