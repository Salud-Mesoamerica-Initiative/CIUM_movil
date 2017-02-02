package code.yoursoft.ciummovil;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzhl.runOnUiThread;


public class AdaptadorEvaluadorRecurso extends RecyclerView.Adapter<ViewHolderER>
{
     List<DataEvaluadorRecurso> paletas;
    //private List<EvaluacionRecursoCriterio> respuestas_db;
    //private List<EvaluacionRecursoCriterio> respuestas_live;

    public boolean cambios=false;
    Funciones link=new Funciones();

    Context context;

    DBManager linkDB = new DBManager(context);

    /// RECIBIR RESPUESTAS DE BASE DATOS
    //  VACIAR  A RESPUESTAS EN VIVO
    //  CONFORME A EVENTOS, ACTUALIZAR LISTA EN VIVO
    /// CREAR METODO QUE DEVUELVA LAS RESPUESTAS EN VIVO Y LAS ENTRGUE A LA UI (EVALUADOR_RECURSO)

    private InterfaceRecurso listener;


    public AdaptadorEvaluadorRecurso(List<DataEvaluadorRecurso> paletas,Context context,InterfaceRecurso listener)
    {

        this.paletas = new ArrayList<>();
        this.paletas.addAll(paletas);

        this.context = context;
        this.listener = listener;

    }

    @Override
    public ViewHolderER onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_er, viewGroup, false);

        return new ViewHolderER(itemView);
    }

    @Override
        public void onBindViewHolder(final ViewHolderER pVH, int i)
    {

        printRespuestas();


        final DataEvaluadorRecurso paleta = paletas.get(i);
        int valor_aprobado=paleta.respuesta;

        System.out.println("\n  **********  [ SE CREA VIEWHOLDER [ "+i+" ], VALOR APROBADO -->  [" + valor_aprobado + "]");
        pVH.radio_group.check(valor_aprobado);


        if(paleta.tipo_item.equals("LUGAR"))
        {
            //pVH.tipo_item.setText("Lugar de verificación : ");
            pVH.tipo_item.setVisibility(View.GONE);
            pVH.nombre_lugar.setText(paleta.nombre_lugar_verificacion);

            pVH.nombre_criterio.setVisibility(View.GONE);
            pVH.nombre_lugar.setVisibility(View.VISIBLE);
            pVH.radio_group.setVisibility(View.GONE);

            pVH.radio_si.setChecked(false);
            pVH.radio_no.setChecked(false);
            pVH.radio_na.setChecked(false);

        }
        if(paleta.tipo_item.equals("CRITERIO"))
        {
               //pVH.tipo_item.setText("Criterio : ");
               pVH.tipo_item.setVisibility(View.GONE);
               pVH.nombre_criterio.setText(paleta.nombre_criterio);

               pVH.nombre_lugar.setVisibility(View.GONE);
               pVH.nombre_criterio.setVisibility(View.VISIBLE);
               pVH.radio_group.setVisibility(View.VISIBLE);

                if(paleta.habilitar_na==0)
                {
                    pVH.radio_na.setVisibility(View.GONE);
                }else{
                       pVH.radio_na.setVisibility(View.VISIBLE);
                     }

             }

        if(valor_aprobado==1)
        {
            pVH.radio_si.setChecked(true);
            pVH.radio_no.setChecked(false);
            pVH.radio_na.setChecked(false);
        }
        if(valor_aprobado==0)
        {
            pVH.radio_si.setChecked(false);
            pVH.radio_no.setChecked(true);
            pVH.radio_na.setChecked(false);
        }
        if(valor_aprobado==2)
        {
            pVH.radio_si.setChecked(false);
            pVH.radio_no.setChecked(false);
            pVH.radio_na.setChecked(true);
        }
        if(valor_aprobado==-1)
        {
            pVH.radio_si.setChecked(false);
            pVH.radio_no.setChecked(false);
            pVH.radio_na.setChecked(false);
        }
        //pVH.radio_group.check(valor_aprobado);



        pVH.radio_si.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                cambios=true;
                ///link.printToast("CLICK YES BUTTON WITH CLICK", pVH.radio_si.getContext());

                int id_evaluacion_r = paleta.id_evaluacion_recurso;
                int id_indicador = paleta.id_indicador;
                int id_criterio = paleta.id_criterio;


                int pos = 0;

                for (int i = 0; i < paletas.size(); i++)
                {
                    if (id_indicador == paletas.get(i).id_indicador
                            && id_criterio == paletas.get(i).id_criterio
                            && id_evaluacion_r == paletas.get(i).id_evaluacion_recurso)
                    {
                        pos = i;
                    }

                }//FIN FOR RESPUESTAS DB Ó ACTUALES

                //notifyItemChanged(i);

                System.out.println("\n   POS [" + pos + "] SE ACTUALIZARÁ -->  RESPUESTA [ 0 ]");

                paletas.set(pos, new DataEvaluadorRecurso(

                                                            paletas.get(pos).id_evaluacion_recurso,
                                                            "CRITERIO",
                                                            paletas.get(pos).id_indicador,
                                                            paletas.get(pos).id_lugar_verificacion,
                                                            paletas.get(pos).nombre_lugar_verificacion,
                                                            paletas.get(pos).id_criterio,
                                                            paletas.get(pos).nombre_criterio,
                                                            paletas.get(pos).habilitar_na,
                                                            1, ///RESPUESTA SI
                                                            paletas.get(pos).id_erc,
                                                            1
                                                           ));
                //notifyItemChanged(pos);
                printRespuestas();
                listener.avance_validacion();

            }
        });

        pVH.radio_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cambios =true;

                int id_evaluacion_r = paleta.id_evaluacion_recurso;
                int id_indicador = paleta.id_indicador;
                int id_criterio = paleta.id_criterio;


                int pos = 0;

                for (int i = 0; i < paletas.size(); i++)
                {
                    if (id_indicador == paletas.get(i).id_indicador
                            && id_criterio == paletas.get(i).id_criterio
                            && id_evaluacion_r == paletas.get(i).id_evaluacion_recurso)
                    {
                        pos = i;
                    }

                }//FIN FOR RESPUESTAS DB Ó ACTUALES

                //notifyItemChanged(i);

                System.out.println("\n   POS [" + pos + "] SE ACTUALIZARÁ -->  RESPUESTA [ 1 ]");

                paletas.set(pos, new DataEvaluadorRecurso(
                                                            paletas.get(pos).id_evaluacion_recurso,
                                                            "CRITERIO",
                                                            paletas.get(pos).id_indicador,
                                                            paletas.get(pos).id_lugar_verificacion,
                                                            paletas.get(pos).nombre_lugar_verificacion,
                                                            paletas.get(pos).id_criterio,
                                                            paletas.get(pos).nombre_criterio,
                                                            paletas.get(pos).habilitar_na,
                                                            0, // respuesta no
                                                            paletas.get(pos).id_erc,
                                                            1

                                                            ));
                //notifyItemChanged(pos);
                printRespuestas();
                listener.avance_validacion();

            }
        });


        pVH.radio_na.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                cambios = true;

                int id_evaluacion_r = paleta.id_evaluacion_recurso;
                int id_indicador = paleta.id_indicador;
                int id_criterio = paleta.id_criterio;


                int pos = 0;

                for (int i = 0; i < paletas.size(); i++)
                {
                    if (id_indicador == paletas.get(i).id_indicador
                            && id_criterio == paletas.get(i).id_criterio
                            && id_evaluacion_r == paletas.get(i).id_evaluacion_recurso)
                    {
                        pos = i;
                    }

                }//FIN FOR RESPUESTAS DB Ó ACTUALES


                System.out.println("\n   POS [" + pos + "] SE ACTUALIZARÁ -->  RESPUESTA [ 2 ]");
                paletas.set(pos, new DataEvaluadorRecurso(

                        paletas.get(pos).id_evaluacion_recurso,
                        "CRITERIO",
                        paletas.get(pos).id_indicador,
                        paletas.get(pos).id_lugar_verificacion,
                        paletas.get(pos).nombre_lugar_verificacion,
                        paletas.get(pos).id_criterio,
                        paletas.get(pos).nombre_criterio,
                        paletas.get(pos).habilitar_na,
                        2,//respuesta na
                        paletas.get(pos).id_erc,
                        1

                ));


                printRespuestas();
                listener.avance_validacion();

            }
        });



        pVH.radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                //link.printToast("CAMBIO DE VALOR DETECTADO -- " + paleta.nombre_criterio, group.getContext());

            }
        });



    }

    @Override
    public int getItemCount()
    {
        return paletas.size();
    }

    public List<DataEvaluadorRecurso> getRespuestas()
    {
        List<DataEvaluadorRecurso> respuestas = new ArrayList<>();
        respuestas.addAll(this.paletas);

        return respuestas;
    }

/*
    public int contarPreguntas()
    {
      int preguntas=0;

        for(int i=0; i<paletas.size(); i++)
        {
            if(paletas.get(i).tipo_item.equals("CRITERIO"))
            {
                preguntas++;
            }

        }


      return preguntas;
    }

 public int contarRespuestas()
    {
        int respuestas=0;

        for(int i=0; i<paletas.size(); i++)
        {
            if(paletas.get(i).tipo_item.equals("CRITERIO"))
            {
                if(paletas.get(i).respuesta==0 || paletas.get(i).respuesta==1 || paletas.get(i).respuesta==2)
                {
                    respuestas++;
                }
            }
        }

        return respuestas;
    }

    */

    public int [] contarPreguntas()
    {
        int preguntas=0;
        int respondidas=0;
        int aprobadas=0;
        int negativas=0;
        int nas=0;

        int changes=0;

        if(cambios==true){changes=1;}else{changes=0;}

        for(int i=0; i<paletas.size(); i++)
        {
            if(paletas.get(i).tipo_item.equals("CRITERIO"))
            {

                preguntas++;

                if(paletas.get(i).respuesta==1)
                {
                    aprobadas++;
                    respondidas++;
                }

                if(paletas.get(i).respuesta==0)
                {
                    negativas++;
                    respondidas++;
                }

                if(paletas.get(i).respuesta==2)
                {
                    nas++;
                    respondidas++;
                }

            }

        }

        int array []={preguntas,respondidas,aprobadas,negativas,nas,changes};

        return array;
    }


    public void printRespuestas()
    {

        System.out.println("\n ------ INICIO IMPRESION RESPUESTAS LIVE :   -------->  ");
        for(int j=0; j<paletas.size();j++)
        {
            System.out.println( "ID EVAL. REC : "+paletas.get(j).id_evaluacion_recurso+
                            ", ID INDICADOR: "+paletas.get(j).id_indicador+
                            ", ID CRITERIO : ["+paletas.get(j).id_criterio+"] VALUE --> ["+paletas.get(j).respuesta+"]");

        }
        System.out.println("\n <----- FIN IMPRESION RESPUESTAS LIVE -----");

    }


    public void updateDataRecycler(List<DataEvaluadorRecurso> new_preguntas)
    {
        this.paletas.clear();
        this.paletas.addAll(new_preguntas);

        notifyDataSetChanged();
    }


    public void addItem(DataEvaluadorRecurso item, int index)
    {
        this.paletas.add(item);
        notifyItemInserted(index);
    }

    public void deleteItem(int index)
    {
        this.paletas.remove(index);
        notifyItemRemoved(index);
    }



}



 class ViewHolderER  extends RecyclerView.ViewHolder
{

    protected TextView nombre_lugar;
    protected TextView nombre_criterio;
    protected TextView tipo_item;

    //protected CardView card;
    protected RadioGroup radio_group;
    protected RadioButton radio_si;
    protected RadioButton radio_no;
    protected RadioButton radio_na;


    public ViewHolderER(View itemView)
    {
        super(itemView);

        nombre_lugar = (TextView) itemView.findViewById(R.id.nombre_lugar);
        nombre_criterio = (TextView) itemView.findViewById(R.id.nombre_criterio);

        tipo_item = (TextView) itemView.findViewById(R.id.tipo_item);

        radio_group = (RadioGroup) itemView.findViewById(R.id.radio_group);
        radio_si = (RadioButton) itemView.findViewById(R.id.si);
        radio_no = (RadioButton) itemView.findViewById(R.id.no);
        radio_na = (RadioButton) itemView.findViewById(R.id.na);

       // card = (CardView) itemView;
    }

}


interface InterfaceRecurso {

    public void avance_validacion();

}