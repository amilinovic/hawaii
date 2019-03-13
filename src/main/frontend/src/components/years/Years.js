import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import { requestYears } from '../../store/actions/yearsActions';
import { getYears } from '../../store/selectors';
import withResetOnNavigate from '../HOC/withResetOnNavigate';
import Loading from '../loading/Loading';
import YearItem from './YearItem';

class Teams extends Component {
  componentDidMount() {
    this.props.requestYears();
  }

  render() {
    if (!this.props.years) return <Loading />;

    const yearItems = this.props.years.map(item => {
      return <YearItem key={item.yearId} year={item} />;
    });

    return (
      <div className="container-fluid">
        <div className="row">
          <div className="col-md-12">
            <NavLink to={'/years/create'}>
              <input
                type="button"
                value="Create Year"
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
                  <th>Year</th>
                </tr>
              </thead>
              <tbody>{yearItems}</tbody>
            </table>
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  years: getYears(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestYears }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(Teams));
