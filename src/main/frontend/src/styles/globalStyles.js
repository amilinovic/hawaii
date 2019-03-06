import { injectGlobal } from 'styled-components';

injectGlobal`
  @import url('https://fonts.googleapis.com/css?family=Montserrat');

*{
  user-select: none;	
}	

html, body {
  margin: 0;
  padding: 0;
  display: block;
  width: 100%;
  height: 100%;
}


body {	
  font-family: 'Montserrat', sans-serif;
  -webkit-font-smoothing: antialiased;
}

#root {
  width: 100%;
  height: 100%;
}

.root-wrapper {
  display: flex;
  flex-direction: row;
  height: 100%;
  width: 100%;
  overflow: hidden;
}

a {	
  text-decoration: none;	
  display: block;
  transition: 0.2s ease-in-out;
  &:hover,	
  &:focus,	
  &:active {	
    text-decoration: none;	
  }	
  &.active {
    background: #E45052;
  }
}	
	
button {	
  border: none;	
  &:hover,	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}	
	
img {	
  max-width: 100%;	
}
	
textarea {	
  resize: none;	
  overflow-y: hidden;	
  &:focus,	
  &:active {	
    outline: none;	
  }	
}	

input {	
    &:focus,	
    &:active {	
      outline: none;	
    }	
  }	
`;
