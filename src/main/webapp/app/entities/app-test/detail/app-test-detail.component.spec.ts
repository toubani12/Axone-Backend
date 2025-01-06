import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AppTestDetailComponent } from './app-test-detail.component';

describe('AppTest Management Detail Component', () => {
  let comp: AppTestDetailComponent;
  let fixture: ComponentFixture<AppTestDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppTestDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AppTestDetailComponent,
              resolve: { appTest: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AppTestDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AppTestDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load appTest on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AppTestDetailComponent);

      // THEN
      expect(instance.appTest()).toEqual(expect.objectContaining({ id: 123 }));
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
