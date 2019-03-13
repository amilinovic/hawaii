import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { requestPublicHolidays } from '../../store/actions/publicHolidaysActions';
import { getPublicHolidays } from '../../store/selectors';
import withResetOnNavigate from '../HOC/withResetOnNavigate';
import Loading from '../loading/Loading';
import PublicHolidayItem from './PublicHolidaysItem';

class PublicHolidays extends Component {
  componentDidMount() {
    this.props.requestPublicHolidays();
  }

  render() {
    if (!this.props.publicHolidays) return <Loading />;

    const publicHolidaysItems = this.props.publicHolidays.map(item => {
      return <PublicHolidayItem key={item.date} publicHoliday={item} />;
    });

    return (
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <NavLink to={'/teams/create'}>
              <input
                type="button"
                value="Create Team"
                className="btn btn-primary float-right"
              />
            </NavLink>
          </div>
        </div>
        <div className="row">
          <div className="col-md-12">
            <table className="table table-hover">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Date</th>
                </tr>
              </thead>
              <tbody>{publicHolidaysItems}</tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  publicHolidays: getPublicHolidays(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestPublicHolidays }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(PublicHolidays));
