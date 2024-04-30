/// <reference types="cypress" />
describe('Form tests', () => {
    beforeEach(() => {
        // Visit the login page
        cy.visit('/login')
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
    
        // Intercept the session request
        cy.intercept('GET', '/api/session', {
            body:[ {
              id: 2,
              name: "Did admin",
              date: "2024-04-06T00:00:00.000+00:00",
              teacher_id: 2,
              description: "session yoga v2 v3",
              users: [1,3],
              createdAt: "2024-04-05T15:25:49",
              updatedAt: "2024-04-10T14:52:03"
            }],
            }).as('sessions')

            cy.intercept('GET', '/api/session/2', {
                body:{
                    id: 2,
                    name: "Did admin",
                    date: "2024-04-06T00:00:00.000+00:00",
                    teacher_id: 2,
                    description: "session yoga v2 v3",
                    users: [1,3],
                    createdAt: "2024-04-05T15:25:49",
                    updatedAt: "2024-04-10T14:52:03"
                  },
                  }).as('session') 

            cy.intercept('GET', '/api/teacher', {
                body:[
                    {
                        id: 1,
                        lastName: "DELAHAYE",
                        firstName: "Margot",
                        createdAt: "2024-04-24T20:18:07",
                        updatedAt: "2024-04-24T20:18:07"
                    },
                    {
                        id: 2,
                        lastName: "THIERCELIN",
                        firstName: "Hélène",
                        createdAt: "2024-04-24T20:18:07",
                        updatedAt: "2024-04-24T20:18:07"
                    }
                ],
                }).as('teacher') 
    
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.url().should('include', '/sessions')   
    })

    it('Form update should be displayed', () => {
        cy.intercept('PUT', '/api/session/2', {
            body: {
                id: 2,
                name: "session 2",
                date: "2012-01-01T00:00:00.000+00:00",
                teacher_id: 1,
                description: "my description",
                users: [],
                createdAt: null,
                updatedAt: "2024-04-24T20:24:03.446011"
            },
        })
        cy.contains('Edit').click()
        cy.url().should('include', '/sessions/update/2')
        cy.contains('Save').click()
    })

    it('Form create should be displayed', () => {
        cy.intercept('POST', '/api/session', {
            body: {
                id: 3,
                name: "session 3",
                date: "2012-01-01T00:00:00.000+00:00",
                teacher_id: null,
                description: "my description",
                users: [],
                createdAt: "2024-04-24T20:24:03.444995",
                updatedAt: "2024-04-24T20:24:03.446011"
            },
        })
        cy.contains('Create').click()
        cy.url().should('include', '/sessions/create')

        cy.get('input[formControlName=name]').type("session 3")
        cy.get('input[formControlName=date]').type("2012-01-01")
        cy.get('textarea[formControlName=description]').type("my description")
        cy.get('mat-select[formControlName=teacher_id]').click();
        cy.get('mat-option').contains('Hélène THIERCELIN').click();
        cy.contains('Save').click()
    })

})
