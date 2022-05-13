const TEMPLATE = {
    "id": 1,
    "uuid": "ffef9e06-1271-4079-af9e-0612715079e9",
    "model": "d",
    "airbags": [
        {
            "name": "driver",
            "value": false
        },
        {
            "name": "passenger",
            "value": false
        },
        {
            "name": "back-seats",
            "value": false
        },
        {
            "name": "back-sides",
            "value": false
        }
    ],
    "year": 2003,
    "colour": "#000000",
    "invoiceGenerated": false
};

function init()
{
    document.getElementById( "generate-cars" ).addEventListener( "click", generateCars );
    loadCars();
}

async function loadCars()
{
    const table = document.getElementById( "cars" );
    const children = [ ...table.children ];
    for( let i = 0; i < children.length; i++ ) {
        table.removeChild( children[i] );
    }
    let cars = await asyncFetchJSON( "/json" );
    cars.map( ( car, i ) =>{
        table.appendChild( tableRow( car ) );
    } );
}

async function generateCars()
{
    await asyncFetchJSON( "/generate", TEMPLATE );
    await loadCars();
}


function tableRow( car )
{
    let row = document.createElement( "tr" );
    Object.keys( car ).map( ( key, i )=> {
        let cell = document.createElement( "td" );
        let text = "";
        if( Array.isArray( car[ key ] ) )
        {
            car[ key ].map( ( el )=>{ text += el.name + ": "; text += el.value + "\n" } );
        }
        else if( key === "invoiceGenerated" )
            return;
        else if( key === "colour" )
        {
            cell.style.backgroundColor = car[ key ];
        }
        else
            text = car[ key ];
        cell.innerText = text;
        row.appendChild( cell );
    } );
    let cell = document.createElement( "td" );
    let btn = document.createElement( "button" );
    btn.innerText = "Generuj fakturę VAT";
    btn.addEventListener( "click", async ()=>{ await asyncFetchJSON( "/invoice", car ); loadCars() } )
    cell.appendChild( btn );
    row.appendChild( cell );

    cell = document.createElement( "td" );
    btn = document.createElement( "a" );
    btn.innerText = "Pobierz";
    btn.href = "invoices/" + car.uuid;
    btn.style.visibility = car.invoiceGenerated ? "visible" : "hidden";
    cell.appendChild( btn );
    row.appendChild( cell );
    return row;
}

async function asyncFetchJSON( target, data = {} )
{
    const options = {
        method: "POST",
        body: JSON.stringify( data )
    };
    let res = await fetch( target, options );
    if( !res.ok )
        return res.status;
    return await res.json();
}

async function getInvoice( target )
{
    const options = {
        method: "GET",
    };
    let res = await fetch( target, options );
    if( !res.ok )
        return res.status;
    console.log(res)
    return res;
}