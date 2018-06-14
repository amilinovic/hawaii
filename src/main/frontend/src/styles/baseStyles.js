import { injectGlobal } from 'styled-components';

export default () => injectGlobal`

  @import url('https://fonts.googleapis.com/css?family=Montserrat:400,900|Roboto');

*{
  user-select: none;	
}	
	
body {	
  -webkit-font-smoothing: antialiased;	
}	
	
a {	
  text-decoration: none;	
  &:hover,	
  &:focus,	
  &:active {	
    text-decoration: none;	
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
`;
