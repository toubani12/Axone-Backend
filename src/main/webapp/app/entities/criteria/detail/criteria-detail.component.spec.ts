import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CriteriaDetailComponent } from './criteria-detail.component';

describe('Criteria Management Detail Component', () => {
  let comp: CriteriaDetailComponent;
  let fixture: ComponentFixture<CriteriaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CriteriaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CriteriaDetailComponent,
              resolve: { criteria: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CriteriaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CriteriaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load criteria on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CriteriaDetailComponent);

      // THEN
      expect(instance.criteria()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
