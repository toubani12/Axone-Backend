import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TechCVProjectDetailComponent } from './tech-cv-project-detail.component';

describe('TechCVProject Management Detail Component', () => {
  let comp: TechCVProjectDetailComponent;
  let fixture: ComponentFixture<TechCVProjectDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TechCVProjectDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TechCVProjectDetailComponent,
              resolve: { techCVProject: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TechCVProjectDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TechCVProjectDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load techCVProject on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TechCVProjectDetailComponent);

      // THEN
      expect(instance.techCVProject()).toEqual(expect.objectContaining({ id: 123 }));
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
