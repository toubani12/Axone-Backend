import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InterviewDetailComponent } from './interview-detail.component';

describe('Interview Management Detail Component', () => {
  let comp: InterviewDetailComponent;
  let fixture: ComponentFixture<InterviewDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InterviewDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InterviewDetailComponent,
              resolve: { interview: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InterviewDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InterviewDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load interview on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InterviewDetailComponent);

      // THEN
      expect(instance.interview()).toEqual(expect.objectContaining({ id: 123 }));
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
