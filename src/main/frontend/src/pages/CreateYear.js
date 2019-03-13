import { Formik } from 'formik';
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as Yup from 'yup';
import withResetOnNavigate from '../components/HOC/withResetOnNavigate';
import { createYear } from '../store/actions/yearActions';

const validationSchema = Yup.object().shape({
  year: Yup.string().required()
});

class CreateYear extends Component {
  render() {
    return (
      <div className="d-flex p-4 justify-content-center flex-column">
        <Formik
          validationSchema={validationSchema}
          initialValues={{
            //   TODO: Create doesn't work, fix it!!!
            year: 2021,
            active: true,
            allowances: [],
            yearId: 7
          }}
          onSubmit={this.props.createYear}
          render={({ handleSubmit, handleChange, values, errors, touched }) => (
            <React.Fragment>
              {console.log(values)}
              <input
                className={`${
                  errors.year && touched.year ? 'border-danger' : ''
                } w-100 mb-3 border`}
                name="year"
                defaultValue={values.year}
                placeholder="Year"
                onChange={handleChange}
              />
              <button className="btn" onClick={handleSubmit} type="submit">
                Create year
              </button>
            </React.Fragment>
          )}
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch =>
  bindActionCreators({ createYear }, dispatch);

export default connect(
  null,
  mapDispatchToProps
)(withResetOnNavigate()(CreateYear));
