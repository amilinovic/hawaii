import React, { Component } from 'react';
import { connect } from 'react-redux';
import { NavLink } from 'react-router-dom';
import { bindActionCreators } from 'redux';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import { removeYear, requestYear } from '../store/actions/yearActions';
import { getYear } from '../store/selectors';

class Year extends Component {
  componentDidMount() {
    this.props.requestYear(this.props.match.params.id);
  }

  render() {
    if (!this.props.year) return <Loading />;

    const {
      year: {
        //   TODO: Change this to id once it's done on backend
        yearId,
        year,
        active
      }
    } = this.props;

    return (
      <div className="d-flex flex-grow-1 p-4 flex-column align-items-center">
        <h1>Year</h1>
        <h5 className="text-danger mb-3">{year}</h5>
        <h1>Active</h1>
        <h5 className="text-danger mb-3">{active.toString()}</h5>
        <div>
          <button className="mr-3 btn">
            <NavLink to={`/years/${yearId}/edit`}>Edit</NavLink>
          </button>
          <button
            className="btn btn-danger"
            onClick={() =>
              this.props.removeYear({
                id: yearId
              })
            }
          >
            Delete
          </button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  year: getYear(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestYear, removeYear }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(Year));
