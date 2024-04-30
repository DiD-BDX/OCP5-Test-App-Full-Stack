/// <reference types="cypress" />
describe('me component', () => {
    
    beforeEach(() => {
        // Visit the login page
        cy.visit('/login')
    
        // Intercept the session request
        cy.intercept('GET', '/api/session', {
            body:[ {
              id: 2,
              name: "DiD Admin",
              date: "2024-04-06T00:00:00.000+00:00",
              teacher_id: 2,
              description: "session yoga v2 v3",
              users: [1,3,5,6],
              createdAt: "2024-04-05T15:25:49",
              updatedAt: "2024-04-10T14:52:03"
            }],
            }).as('sessions')
    
        // Intercept the me request
        cy.intercept('GET', '/api/me', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
            },
        }).as('me')
    })

    it('should display admin information after login', () => {
        // Intercept the login request
        cy.intercept('POST', '/api/auth/login', {
            body: {
            id: 2,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
            },
        })
      cy.intercept('GET', '/api/user/2', {
        body: {
        id: 2,
        username: 'userName',
        firstName: 'DiD',
        lastName: 'Admin',
        email: 'user@email.com',
        createdAt: '2021-09-01T00:00:00.000Z',
        updatedAt: '2021-09-01T00:00:00.000Z',
        admin: true
        },
        }).as('user')
  
      // Fill in the login form and submit
      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}`)
      cy.contains('Submit').click()
  
      // Click on the Account link to go to the me page
      cy.contains('Account').click()
  
      // Check that the URL includes '/me'
      cy.url().should('include', '/me')
  
      // Check that the user information is displayed
      cy.contains('DiD')
      cy.contains('ADMIN')
      cy.contains('email')
    })

    it('should display user information after login', () => {
        // Intercept the login request
        cy.intercept('POST', '/api/auth/login', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: false
            },
        })
        cy.intercept('GET', '/api/user/1', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'user@email.com',
            createdAt: '2021-09-01T00:00:00.000Z',
            updatedAt: '2021-09-01T00:00:00.000Z',
            admin: false
            },
            }).as('user')

        // Fill in the login form and submit
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.contains('Submit').click()
    
        // Click on the Account link to go to the me page
        cy.contains('Account').click()
    
        // Check that the URL includes '/me'
        cy.url().should('include', '/me')
    
        // Check that the user information is displayed
        cy.contains('firstName')
        cy.contains('LASTNAME')
        cy.contains('email')
    })

    it('should delete the user', () => {
        // Intercept the login request
        cy.intercept('POST', '/api/auth/login', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: false
            },
        })
        cy.intercept('GET', '/api/user/1', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'user@email.com',
            createdAt: '2021-09-01T00:00:00.000Z',
            updatedAt: '2021-09-01T00:00:00.000Z',
            admin: false
            },
            }).as('user')

        cy.intercept('DELETE', '/api/user/1', {}).as('deleteUser')

        // Fill in the login form and submit
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.contains('Submit').click()
        // Click on the Account link to go to the me page
        cy.contains('Account').click()
        cy.contains('Detail').click()
        
        cy.url().should('include', '/')
    })
    it('should go back to the sessions page', () => {
        // Intercept the login request
        cy.intercept('POST', '/api/auth/login', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: false
            },
        })
        cy.intercept('GET', '/api/user/1', {
            body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'user@email.com',
            createdAt: '2021-09-01T00:00:00.000Z',
            updatedAt: '2021-09-01T00:00:00.000Z',
            admin: false
            },
            }).as('user')

        // Fill in the login form and submit
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}`)
        cy.contains('Submit').click()
        cy.contains('Account').click()
        cy.get('button.mdc-icon-button:contains("arrow_back")').click()
        cy.url().should('include', '/sessions')
    })
})