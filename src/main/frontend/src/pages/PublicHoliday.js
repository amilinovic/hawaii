import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import {
  removePublicHoliday,
  requestPublicHoliday
} from '../store/actions/publicHolidayActions';
import { getPublicHoliday } from '../store/selectors';

class PublicHoliday extends Component {
  componentDidMount() {
    this.props.requestPublicHoliday(this.props.match.params.id);
  }

  render() {
    if (!this.props.publicHoliday) return null;

    const {
      publicHoliday: { id, name, date }
    } = this.props;

    return (
      <div className="d-flex flex-grow-1 p-4 flex-column align-items-center">
        <h1>Public holiday name</h1>
        <h5 className="text-danger mb-3">{name}</h5>
        <h1>Date</h1>
        <h5 className="text-danger mb-3">{date}</h5>
        <div>
          <button className="btn mr-3">
            <NavLink to={`/public-holidays/${id}/edit`}>Edit</NavLink>
          </button>
          <button
            className="btn btn-danger"
            onClick={() => this.props.removePublicHoliday({ id })}
          >
            Delete
          </button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  publicHoliday: getPublicHoliday(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestPublicHoliday, removePublicHoliday }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(PublicHoliday));
