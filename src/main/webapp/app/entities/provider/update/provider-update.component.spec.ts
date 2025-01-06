import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppAccount } from 'app/entities/app-account/app-account.model';
import { AppAccountService } from 'app/entities/app-account/service/app-account.service';
import { ProviderService } from '../service/provider.service';
import { IProvider } from '../provider.model';
import { ProviderFormService } from './provider-form.service';

import { ProviderUpdateComponent } from './provider-update.component';

describe('Provider Management Update Component', () => {
  let comp: ProviderUpdateComponent;
  let fixture: ComponentFixture<ProviderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let providerFormService: ProviderFormService;
  let providerService: ProviderService;
  let appAccountService: AppAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ProviderUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProviderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProviderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    providerFormService = TestBed.inject(ProviderFormService);
    providerService = TestBed.inject(ProviderService);
    appAccountService = TestBed.inject(AppAccountService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppAccount query and add missing value', () => {
      const provider: IProvider = { id: 456 };
      const appAccounts: IAppAccount[] = [{ id: 23270 }];
      provider.appAccounts = appAccounts;

      const appAccountCollection: IAppAccount[] = [{ id: 14643 }];
      jest.spyOn(appAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: appAccountCollection })));
      const additionalAppAccounts = [...appAccounts];
      const expectedCollection: IAppAccount[] = [...additionalAppAccounts, ...appAccountCollection];
      jest.spyOn(appAccountService, 'addAppAccountToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      expect(appAccountService.query).toHaveBeenCalled();
      expect(appAccountService.addAppAccountToCollectionIfMissing).toHaveBeenCalledWith(
        appAccountCollection,
        ...additionalAppAccounts.map(expect.objectContaining),
      );
      expect(comp.appAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const provider: IProvider = { id: 456 };
      const appAccount: IAppAccount = { id: 30543 };
      provider.appAccounts = [appAccount];

      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      expect(comp.appAccountsSharedCollection).toContain(appAccount);
      expect(comp.provider).toEqual(provider);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvider>>();
      const provider = { id: 123 };
      jest.spyOn(providerFormService, 'getProvider').mockReturnValue(provider);
      jest.spyOn(providerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: provider }));
      saveSubject.complete();

      // THEN
      expect(providerFormService.getProvider).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(providerService.update).toHaveBeenCalledWith(expect.objectContaining(provider));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvider>>();
      const provider = { id: 123 };
      jest.spyOn(providerFormService, 'getProvider').mockReturnValue({ id: null });
      jest.spyOn(providerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provider: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: provider }));
      saveSubject.complete();

      // THEN
      expect(providerFormService.getProvider).toHaveBeenCalled();
      expect(providerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProvider>>();
      const provider = { id: 123 };
      jest.spyOn(providerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ provider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(providerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppAccount', () => {
      it('Should forward to appAccountService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appAccountService, 'compareAppAccount');
        comp.compareAppAccount(entity, entity2);
        expect(appAccountService.compareAppAccount).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
