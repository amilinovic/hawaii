import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import Loading from '../components/loading/Loading';
import {
  requestPublicHoliday,
  updatePublicHoliday
} from '../store/actions/publicHolidayActions';
import { getPublicHoliday } from '../store/selectors';

const validationSchema = Yup.object().shape({
  name: Yup.string().required(),
  date: Yup.string().required()
});

class EditPublicHoliday extends Component {
  componentDidMount() {
    this.props.requestPublicHoliday(this.props.match.params.id);
  }

  render() {
    if (!this.props.publicHoliday) return <Loading />;

    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={this.props.publicHoliday}
          onSubmit={this.props.updatePublicHoliday}
          enableReinitialize
        >
          {({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              <input
                className={`${
                  errors.name && touched.name ? 'border-danger' : ''
                } mb-3 border`}
                name="name"
                onChange={handleChange}
                defaultValue={values.name}
                placeholder="Public holiday name"
              />
              <input
                className={`${
                  errors.date && touched.date ? 'border-danger' : ''
                } mb-3 border`}
                name="date"
                onChange={handleChange}
                defaultValue={values.date}
                placeholder="Public holiday date"
              />

              <button className="btn" onClick={handleSubmit} type="submit">
                Update
              </button>
            </React.Fragment>
          )}
        </Formik>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  publicHoliday: getPublicHoliday(state)
});

const mapDispatchToProps = dispatch =>
  bindActionCreators({ requestPublicHoliday, updatePublicHoliday }, dispatch);

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withResetOnNavigate()(EditPublicHoliday));
