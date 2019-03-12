import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import { requestLeaveProfile } from '../store/actions/leaveProfileActions';
import { getLeaveProfile } from '../store/selectors';

class LeaveProfile extends Component {
  componentDidMount() {
    this.props.requestLeaveProfile(this.props.match.params.id);
  }

  render() {
    if (!this.props.leaveProfile) return <Loading />;

    const {
      leaveProfile: {
        id,
        name,
        entitlement,
        maxCarriedOver,
        maxBonusDays,
        maxAllowanceFromNextYear,
        training,
        upgradeable,
        comment
      }
    } = this.props;

    return (
      <div className="d-flex flex-grow-1 p-4 flex-column align-items-center">
        <h1>Leave profile name</h1>
        <h5 className="text-danger mb-3">{name}</h5>
        <h1>Entitlement</h1>
        <h5 className="text-danger mb-3">{entitlement}</h5>
        <h1>Max carried over</h1>
        <h5 className="text-danger mb-3">{maxCarriedOver}</h5>
        <h1>Max bonus days</h1>
        <h5 className="text-danger mb-3">{maxBonusDays}</h5>
        <h1>Max allowance from next year</h1>
        <h5 className="text-danger mb-3">{maxAllowanceFromNextYear}</h5>
        <h1>Training</h1>
        <h5 className="text-danger mb-3">{training}</h5>
        <h1>Comment</h1>
        <h5 className="text-danger mb-3">{comment}</h5>
        <div>
          <button className={`${!upgradeable ? ' mr-3' : ''} btn`}>
            <NavLink to={`/leave-profiles/${id}/edit`}>Edit</NavLink>
          </button>
          {!upgradeable && (
            <button
              className="btn btn-danger"
              onClick={() =>
                this.props.removeTeam({ id: this.props.match.params.id })
              }
            >
              Delete
            </button>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  leaveProfile: getLeaveProfile(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestLeaveProfile }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(LeaveProfile));
