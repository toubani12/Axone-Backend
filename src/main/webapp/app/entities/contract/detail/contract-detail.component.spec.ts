import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContractDetailComponent } from './contract-detail.component';

describe('Contract Management Detail Component', () => {
  let comp: ContractDetailComponent;
  let fixture: ComponentFixture<ContractDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ContractDetailComponent,
              resolve: { contract: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContractDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContractDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contract on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContractDetailComponent);

      // THEN
      expect(instance.contract()).toEqual(expect.objectContaining({ id: 123 }));
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
